#!/usr/bin/env python3
import sys
from awsglue.utils import getResolvedOptions
from awsglue.context import GlueContext
from awsglue.job import Job
from pyspark.context import SparkContext
from pyspark.sql import functions as F

# ---------------------------
# Job args 
# --------------------------
req = ["JOB_NAME", "sfUrl", "sfUser", "sfPassword"]
opt = ["s3Patients", "s3Diagnoses", "s3Prescriptions"]

args = {}
args.update(getResolvedOptions(sys.argv, req))
for k in opt:
    try:
        args.update(getResolvedOptions(sys.argv, [k]))
    except:
        pass

S3_PATIENTS      = args.get("s3Patients",      "s3://hospital-data-sau/patients.csv")
S3_DIAGNOSES     = args.get("s3Diagnoses",     "s3://hospital-data-sau/diagnoses.json")   # JSON Lines
S3_PRESCRIPTIONS = args.get("s3Prescriptions", "s3://hospital-data-sau/prescriptions.csv")

SF_URL      = args["sfUrl"]
SF_USER     = args["sfUser"]
SF_PASSWORD = args["sfPassword"]

SF_DB = "MEDCARE_DW"
SF_WH = "MEDCARE_WH"

SCHEMA_RAW = "RAW"
SCHEMA_STG = "STAGING"
SCHEMA_CUR = "CURATED"

T_RAW_PAT = "raw_patients"
T_RAW_DG  = "raw_diagnoses"
T_RAW_RX  = "raw_prescriptions"

T_STG_PAT = "stg_patients"
T_STG_DG  = "stg_diagnoses"
T_STG_RX  = "stg_prescriptions"

T_CUR_360 = "patient_360"
T_CUR_SUM = "patient_summary"

# ---------------------------
# Spark/Glue bootstrap
# ---------------------------
sc = SparkContext.getOrCreate()
sc.setLogLevel("WARN")
glueContext = GlueContext(sc)
spark = glueContext.spark_session
job = Job(glueContext)
job.init(args["JOB_NAME"], args)

# ---------------------------
# Snowflake writer
# ---------------------------
def sf_options(schema: str):
    return {
        "sfURL": SF_URL,
        "sfUser": SF_USER,
        "sfPassword": SF_PASSWORD,
        "sfDatabase": SF_DB,
        "sfSchema": schema,
        "sfWarehouse": SF_WH,
    }

def write_sf(df, schema: str, table: str, mode: str):
    (
        df.write
          .format("net.snowflake.spark.snowflake")
          .options(**sf_options(schema))
          .option("dbtable", table)
          .mode(mode)
          .save()
    )

# ---------------------------
# 1) READ SOURCES
# ---------------------------
patients_src = spark.read.option("header", True).option("inferSchema", True).csv(S3_PATIENTS)
diagnoses_src = spark.read.option("multiline", False).json(S3_DIAGNOSES)   # JSONL
prescriptions_src = spark.read.option("header", True).option("inferSchema", True).csv(S3_PRESCRIPTIONS)

# ---------------------------
# 2) WRITE RAW 
#    RAW_PATIENTS: 
#    RAW_PRESCRIPTIONS: 
#    RAW_DIAGNOSES: payload VARIANT + _ingested_at 
# ---------------------------
patients_raw = (
    patients_src
      .withColumn("_ingested_at", F.current_timestamp())
)

prescriptions_raw = (
    prescriptions_src
      .withColumn("_ingested_at", F.current_timestamp())
)

# Pack JSON row as a single struct -> VARIANT
#dg_cols = [F.col(c) for c in diagnoses_src.columns]
#diagnoses_raw = (
#    diagnoses_src
#      .select(F.struct(*dg_cols).alias("payload"))
#      .withColumn("_ingested_at", F.current_timestamp())
#)


#write a JSON string
dg_cols = [F.col(c) for c in diagnoses_src.columns]
diagnoses_raw = (
    diagnoses_src
      .select(F.to_json(F.struct(*dg_cols)).alias("payload"))
      .withColumn("_ingested_at", F.current_timestamp())
)


write_sf(patients_raw,      SCHEMA_RAW, T_RAW_PAT, mode="overwrite")
write_sf(diagnoses_raw,     SCHEMA_RAW, T_RAW_DG,  mode="overwrite")
write_sf(prescriptions_raw, SCHEMA_RAW, T_RAW_RX,  mode="overwrite")

# ---------------------------
# 3) STAGING
# ---------------------------
def trim(df, cols):
    for c in cols:
        if c in df.columns:
            df = df.withColumn(c, F.trim(F.col(c)))
    return df

# STG_PATIENTS (DDL columns):
# patient_id, first_name_masked, last_name_masked, ssn_masked, date_of_birth (DATE),
# gender, city, state, insurance_provider, effective_date (DATE), last_updated (TS)
p_stg = trim(patients_src, ["first_name","last_name","ssn","city","state","insurance_provider"])
p_stg = (
    p_stg
      .withColumn("patient_id", F.col("patient_id"))
      .withColumn("first_name_masked", F.sha2(F.col("first_name").cast("string"), 256))
      .withColumn("last_name_masked",  F.sha2(F.col("last_name").cast("string"),  256))
      .withColumn("ssn_masked",        F.sha2(F.col("ssn").cast("string"),        256))
      .withColumn("date_of_birth",     F.to_date(F.col("date_of_birth")))
      .withColumn("gender",            F.col("gender"))
      .withColumn("city",              F.col("city"))
      .withColumn("state",             F.col("state"))
      .withColumn("insurance_provider",F.col("insurance_provider"))
      .withColumn("effective_date",    F.current_date())
      .withColumn("last_updated",      F.current_timestamp())
      .select("patient_id","first_name_masked","last_name_masked","ssn_masked",
              "date_of_birth","gender","city","state","insurance_provider",
              "effective_date","last_updated")
      .dropDuplicates(["patient_id"])
)

# STG_DIAGNOSES (DDL columns):
# diagnosis_id, patient_id, diagnosis_code, description, diagnosis_date, is_date_missing,
# physician_id, physician_name, physician_specialty, created_at
d = diagnoses_src
# Flatten physician
if "physician.id" in d.columns:
    d = d.withColumn("physician_id",        F.col("physician.id")) \
         .withColumn("physician_name",      F.col("physician.name")) \
         .withColumn("physician_specialty", F.col("physician.specialty")) \
         .drop("physician")
elif "physician" in d.columns:
    d = d.withColumn("physician_id",        F.col("physician.id")) \
         .withColumn("physician_name",      F.col("physician.name")) \
         .withColumn("physician_specialty", F.col("physician.specialty")) \
         .drop("physician")

d_stg = (
    d.withColumn("description", F.when(F.col("description").isNull(), F.lit("Description not available"))
                                 .otherwise(F.col("description")))
     .withColumn("physician_id",        F.when(F.col("physician_id").isNull(),        F.lit("UNKNOWN")).otherwise(F.col("physician_id")))
     .withColumn("physician_name",      F.when(F.col("physician_name").isNull(),      F.lit("UNKNOWN")).otherwise(F.col("physician_name")))
     .withColumn("physician_specialty", F.when(F.col("physician_specialty").isNull(), F.lit("UNKNOWN")).otherwise(F.col("physician_specialty")))
     .withColumn("is_date_missing",     F.col("diagnosis_date").isNull())
     .withColumn("diagnosis_date",      F.to_date(F.when(F.col("diagnosis_date").isNull(), F.lit("1900-01-01"))
                                                     .otherwise(F.col("diagnosis_date"))))
     .withColumn("created_at",          F.current_timestamp())
     .select("diagnosis_id","patient_id","diagnosis_code","description",
             "diagnosis_date","is_date_missing","physician_id","physician_name","physician_specialty","created_at")
     .dropDuplicates(["diagnosis_id"])
)

# STG_PRESCRIPTIONS (DDL columns):
# prescription_id, patient_id, drug_name, dosage, frequency, start_date, end_date, prescribing_physician_id, created_at
r_stg = (
    prescriptions_src
      .withColumn("start_date", F.to_date(F.col("start_date")))
      .withColumn("end_date",   F.to_date(F.col("end_date")))
      .withColumn("created_at", F.current_timestamp())
      .select("prescription_id","patient_id","drug_name","dosage","frequency",
              "start_date","end_date","prescribing_physician_id","created_at")
      .dropDuplicates(["prescription_id"])
)

# Write STAGING (overwrite idempotently)
write_sf(p_stg, SCHEMA_STG, T_STG_PAT, mode="overwrite")
write_sf(d_stg, SCHEMA_STG, T_STG_DG,  mode="overwrite")
write_sf(r_stg, SCHEMA_STG, T_STG_RX,  mode="overwrite")

# ---------------------------
# 4) CURATED 
# ---------------------------
p = p_stg.alias("p")
d = d_stg.alias("d")
r = r_stg.alias("r")

patient_360 = (
    p.join(d, F.col("p.patient_id")==F.col("d.patient_id"), "left")
     .join(r, F.col("p.patient_id")==F.col("r.patient_id"), "left")
     .select(
        F.col("p.patient_id"),
        F.col("p.date_of_birth"),
        F.col("p.gender"),
        F.col("p.city"),
        F.col("p.state"),
        F.col("p.insurance_provider"),
        F.col("d.diagnosis_id"),
        F.col("d.diagnosis_code"),
        F.col("d.description").alias("diagnosis_description"),
        F.col("d.diagnosis_date"),
        F.col("d.physician_id"),
        F.col("d.physician_name"),
        F.col("d.physician_specialty"),
        F.col("r.prescription_id"),
        F.col("r.drug_name"),
        F.col("r.dosage"),
        F.col("r.frequency"),
        F.col("r.start_date").alias("prescription_start_date"),
        F.col("r.end_date").alias("prescription_end_date"),
        F.col("r.prescribing_physician_id"),
        F.current_timestamp().alias("record_created_at"),
     )
)

write_sf(patient_360, SCHEMA_CUR, T_CUR_360, mode="overwrite")

# PATIENT_SUMMARY 
patient_summary = (
    patient_360.groupBy("patient_id").agg(
        F.countDistinct("diagnosis_id").alias("diagnosis_count"),
        F.sum(F.when((F.col("prescription_id").isNotNull()) & (F.col("prescription_end_date").isNull()), 1).otherwise(0)).cast("int")
            .alias("active_prescription_count"),
        F.countDistinct("prescription_id").alias("total_prescription_count"),
        F.concat_ws(",", F.array_sort(F.array_distinct(F.collect_list("diagnosis_code")))).alias("diagnosis_codes"),
        F.concat_ws(",", F.array_sort(F.array_distinct(F.collect_list("drug_name")))).alias("drug_names"),
        F.max("diagnosis_date").alias("latest_diagnosis_date"),
        F.max("prescription_start_date").alias("latest_prescription_date"),
    )
    .withColumn("last_refreshed_at", F.current_timestamp())
)

write_sf(patient_summary, SCHEMA_CUR, T_CUR_SUM, mode="overwrite")

print("ETL finished: RAW, STAGING, CURATED aligned to your Snowflake DDL.")
job.commit()