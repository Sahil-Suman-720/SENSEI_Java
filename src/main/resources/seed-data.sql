-- SENSEI Dummy Data: 100 Teachers
-- Run this after the app has created tables (start the app once first)

-- First, create 100 teacher users
INSERT INTO users (email, password, name, role, auth_provider, created_at, updated_at) VALUES
('teacher1@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Aarav Sharma', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher2@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Priya Patel', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher3@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Rohan Mehta', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher4@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Sneha Gupta', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher5@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Vikram Singh', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher6@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Ananya Reddy', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher7@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Karthik Iyer', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher8@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Divya Nair', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher9@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Arjun Desai', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher10@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Meera Joshi', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher11@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Rahul Kumar', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher12@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Pooja Verma', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher13@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Siddharth Rao', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher14@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Nisha Agarwal', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher15@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Aditya Bhat', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher16@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Kavita Menon', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher17@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Manish Tiwari', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher18@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Ritu Saxena', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher19@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Suresh Pillai', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher20@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Lakshmi Sundaram', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher21@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Deepak Chauhan', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher22@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Swati Kulkarni', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher23@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Nikhil Pandey', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher24@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Anjali Mishra', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher25@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Gaurav Kapoor', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher26@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Tanvi Shah', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher27@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Amit Jain', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher28@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Shruti Banerjee', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher29@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Vishal Thakur', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher30@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Pallavi Hegde', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher31@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Rajesh Mukherjee', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher32@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Neha Srinivasan', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher33@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Harsh Malhotra', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher34@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Vidya Krishnan', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher35@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Pranav Deshpande', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher36@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Simran Kaur', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher37@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Tarun Bhatt', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher38@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Aditi Choudhary', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher39@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Mohan Das', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher40@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Rekha Venkatesh', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher41@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Vivek Narayan', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher42@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Sana Khan', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher43@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Ashwin Rajan', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher44@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Megha Bose', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher45@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Kunal Sethi', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher46@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Bhavna Yadav', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher47@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Dhruv Mathur', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher48@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Ishita Goyal', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher49@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Saurabh Tripathi', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher50@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Radhika Menon', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher51@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Anand Shetty', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher52@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Jaya Lakshmi', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher53@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Naveen Prasad', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher54@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Geeta Ramesh', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher55@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Pankaj Awasthi', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher56@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Uma Subramanian', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher57@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Sachin Wagh', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher58@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Asha Pillai', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher59@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Hemant Shukla', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher60@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Chitra Naidu', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher61@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Varun Saxena', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher62@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Aparna Ghosh', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher63@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Ravi Shankar', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher64@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Sunita Mohan', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher65@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Akash Oberoi', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher66@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Preeti Chopra', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher67@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Sandeep Gill', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher68@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Manju Rani', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher69@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Ajay Bhardwaj', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher70@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Shweta Dixit', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher71@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Manoj Dubey', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher72@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Tara Krishnamurthy', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher73@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Yogesh Patil', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher74@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Sarita Bhandari', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher75@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Prakash Hegde', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher76@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Nandini Rao', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher77@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Girish Nayak', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher78@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Kamala Suresh', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher79@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Rohit Arora', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher80@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Lata Venkatesan', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher81@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Vinod Sharma', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher82@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Deepika Nair', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher83@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Ramesh Iyer', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher84@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Padma Gopal', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher85@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Shantanu Bose', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher86@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Radha Iyer', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher87@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Ashok Menon', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher88@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Jayashree Pillai', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher89@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Dinesh Kulkarni', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher90@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Vasudha Rao', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher91@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Narendra Jha', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher92@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Sumitra Rani', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher93@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Vijay Malhotra', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher94@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Hema Sharma', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher95@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Abhishek Reddy', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher96@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Rukmini Devi', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher97@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Omkar Patnaik', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher98@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Sarala Dutta', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher99@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Krishna Murthy', 'TEACHER', 'LOCAL', NOW(), NOW()),
('teacher100@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Indira Rangan', 'TEACHER', 'LOCAL', NOW(), NOW());

-- Now create teacher profiles (linking to user IDs)
-- We'll use a procedure to auto-link and randomize data

SET @start_id = (SELECT MIN(id) FROM users WHERE email = 'teacher1@sensei.com');

-- Create teacher profiles with varied ratings, bookings, subjects, cities
INSERT INTO teacher_profiles (user_id, bio, hourly_rate, avg_rating, total_bookings, total_reviews, experience_years, city, last_active_at, created_at)
SELECT 
  u.id,
  CONCAT('Experienced educator specializing in personalized learning. ', 
    CASE (u.id % 10)
      WHEN 0 THEN 'I focus on building strong fundamentals and problem-solving skills.'
      WHEN 1 THEN 'My teaching approach combines theory with real-world applications.'
      WHEN 2 THEN 'I believe in adaptive teaching methods tailored to each student.'
      WHEN 3 THEN 'Passionate about making complex topics simple and engaging.'
      WHEN 4 THEN 'Over a decade of experience in both classroom and online teaching.'
      WHEN 5 THEN 'I use interactive methods to keep students engaged and motivated.'
      WHEN 6 THEN 'Dedicated to helping students achieve academic excellence.'
      WHEN 7 THEN 'Expert in exam preparation and competitive test coaching.'
      WHEN 8 THEN 'I provide structured learning paths with regular assessments.'
      ELSE 'Committed to nurturing curiosity and independent thinking.'
    END
  ) as bio,
  ROUND(200 + (RAND() * 1800), 0) as hourly_rate,
  ROUND(2.5 + (RAND() * 2.5), 1) as avg_rating,
  FLOOR(RAND() * 150) as total_bookings,
  FLOOR(RAND() * 80) as total_reviews,
  FLOOR(2 + RAND() * 18) as experience_years,
  ELT(FLOOR(1 + RAND() * 10), 'Mumbai', 'Delhi', 'Bangalore', 'Hyderabad', 'Chennai', 'Pune', 'Kolkata', 'Ahmedabad', 'Jaipur', 'Lucknow') as city,
  DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 30) DAY) as last_active_at,
  DATE_SUB(NOW(), INTERVAL FLOOR(60 + RAND() * 300) DAY) as created_at
FROM users u
WHERE u.role = 'TEACHER' AND u.email LIKE 'teacher%@sensei.com';

-- Now insert subjects for each teacher (2-4 subjects each)
-- Subjects pool: Mathematics, Physics, Chemistry, Biology, English, Computer Science, Economics, Guitar, Piano, Yoga, Photography, Cooking, Hindi, French, Spanish, Drawing, History, Geography

INSERT INTO teacher_subjects (teacher_id, subject)
SELECT tp.id, 
  ELT(FLOOR(1 + RAND() * 18), 'Mathematics', 'Physics', 'Chemistry', 'Biology', 'English', 'Computer Science', 'Economics', 'Guitar', 'Piano', 'Yoga', 'Photography', 'Cooking', 'Hindi', 'French', 'Spanish', 'Drawing', 'History', 'Geography')
FROM teacher_profiles tp;

INSERT INTO teacher_subjects (teacher_id, subject)
SELECT tp.id, 
  ELT(FLOOR(1 + RAND() * 18), 'Mathematics', 'Physics', 'Chemistry', 'Biology', 'English', 'Computer Science', 'Economics', 'Guitar', 'Piano', 'Yoga', 'Photography', 'Cooking', 'Hindi', 'French', 'Spanish', 'Drawing', 'History', 'Geography')
FROM teacher_profiles tp;

INSERT INTO teacher_subjects (teacher_id, subject)
SELECT tp.id, 
  ELT(FLOOR(1 + RAND() * 18), 'Mathematics', 'Physics', 'Chemistry', 'Biology', 'English', 'Computer Science', 'Economics', 'Guitar', 'Piano', 'Yoga', 'Photography', 'Cooking', 'Hindi', 'French', 'Spanish', 'Drawing', 'History', 'Geography')
FROM teacher_profiles tp
WHERE RAND() > 0.4;

-- Remove duplicate subjects per teacher
DELETE t1 FROM teacher_subjects t1
INNER JOIN teacher_subjects t2
WHERE t1.teacher_id = t2.teacher_id AND t1.subject = t2.subject AND t1.teacher_id > t2.teacher_id;

-- Verify
SELECT COUNT(*) as total_teachers FROM teacher_profiles;
SELECT COUNT(*) as total_subjects FROM teacher_subjects;
