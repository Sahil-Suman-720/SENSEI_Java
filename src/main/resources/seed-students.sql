-- SENSEI Dummy Students: 20 students with varied interests
-- Run this after seed-data.sql

INSERT INTO users (email, password, name, role, auth_provider, phone, created_at, updated_at) VALUES
('student1@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Anika Verma', 'STUDENT', 'LOCAL', '9876543210', NOW(), NOW()),
('student2@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Dev Kapoor', 'STUDENT', 'LOCAL', '9876543211', NOW(), NOW()),
('student3@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Isha Nair', 'STUDENT', 'LOCAL', '9876543212', NOW(), NOW()),
('student4@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Kabir Singh', 'STUDENT', 'LOCAL', '9876543213', NOW(), NOW()),
('student5@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Myra Joshi', 'STUDENT', 'LOCAL', '9876543214', NOW(), NOW()),
('student6@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Reyansh Patel', 'STUDENT', 'LOCAL', '9876543215', NOW(), NOW()),
('student7@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Sara Khan', 'STUDENT', 'LOCAL', '9876543216', NOW(), NOW()),
('student8@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Vihaan Reddy', 'STUDENT', 'LOCAL', '9876543217', NOW(), NOW()),
('student9@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Zara Sharma', 'STUDENT', 'LOCAL', '9876543218', NOW(), NOW()),
('student10@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Aryan Mehta', 'STUDENT', 'LOCAL', '9876543219', NOW(), NOW()),
('student11@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Diya Iyer', 'STUDENT', 'LOCAL', '9876543220', NOW(), NOW()),
('student12@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Farhan Ali', 'STUDENT', 'LOCAL', '9876543221', NOW(), NOW()),
('student13@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Gauri Desai', 'STUDENT', 'LOCAL', '9876543222', NOW(), NOW()),
('student14@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Harsh Bansal', 'STUDENT', 'LOCAL', '9876543223', NOW(), NOW()),
('student15@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Inaya Pillai', 'STUDENT', 'LOCAL', '9876543224', NOW(), NOW()),
('student16@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Jay Kulkarni', 'STUDENT', 'LOCAL', '9876543225', NOW(), NOW()),
('student17@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Kiara Bose', 'STUDENT', 'LOCAL', '9876543226', NOW(), NOW()),
('student18@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Laksh Gupta', 'STUDENT', 'LOCAL', '9876543227', NOW(), NOW()),
('student19@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Mahi Thakur', 'STUDENT', 'LOCAL', '9876543228', NOW(), NOW()),
('student20@sensei.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Nakul Shetty', 'STUDENT', 'LOCAL', '9876543229', NOW(), NOW());

-- Now add varied interests for each student
-- Each student gets a different combination so recommendations differ

-- Student 1: Science-focused
INSERT INTO user_interests (user_id, interest) SELECT id, 'Mathematics' FROM users WHERE email='student1@sensei.com';
INSERT INTO user_interests (user_id, interest) SELECT id, 'Physics' FROM users WHERE email='student1@sensei.com';
INSERT INTO user_interests (user_id, interest) SELECT id, 'Chemistry' FROM users WHERE email='student1@sensei.com';

-- Student 2: Creative arts
INSERT INTO user_interests (user_id, interest) SELECT id, 'Guitar' FROM users WHERE email='student2@sensei.com';
INSERT INTO user_interests (user_id, interest) SELECT id, 'Piano' FROM users WHERE email='student2@sensei.com';
INSERT INTO user_interests (user_id, interest) SELECT id, 'Drawing' FROM users WHERE email='student2@sensei.com';

-- Student 3: Languages
INSERT INTO user_interests (user_id, interest) SELECT id, 'English' FROM users WHERE email='student3@sensei.com';
INSERT INTO user_interests (user_id, interest) SELECT id, 'French' FROM users WHERE email='student3@sensei.com';
INSERT INTO user_interests (user_id, interest) SELECT id, 'Spanish' FROM users WHERE email='student3@sensei.com';
INSERT INTO user_interests (user_id, interest) SELECT id, 'Hindi' FROM users WHERE email='student3@sensei.com';

-- Student 4: Tech + Math
INSERT INTO user_interests (user_id, interest) SELECT id, 'Computer Science' FROM users WHERE email='student4@sensei.com';
INSERT INTO user_interests (user_id, interest) SELECT id, 'Mathematics' FROM users WHERE email='student4@sensei.com';

-- Student 5: Wellness + Creative
INSERT INTO user_interests (user_id, interest) SELECT id, 'Yoga' FROM users WHERE email='student5@sensei.com';
INSERT INTO user_interests (user_id, interest) SELECT id, 'Cooking' FROM users WHERE email='student5@sensei.com';
INSERT INTO user_interests (user_id, interest) SELECT id, 'Photography' FROM users WHERE email='student5@sensei.com';

-- Student 6: Pure Science
INSERT INTO user_interests (user_id, interest) SELECT id, 'Physics' FROM users WHERE email='student6@sensei.com';
INSERT INTO user_interests (user_id, interest) SELECT id, 'Biology' FROM users WHERE email='student6@sensei.com';
INSERT INTO user_interests (user_id, interest) SELECT id, 'Chemistry' FROM users WHERE email='student6@sensei.com';

-- Student 7: Humanities
INSERT INTO user_interests (user_id, interest) SELECT id, 'History' FROM users WHERE email='student7@sensei.com';
INSERT INTO user_interests (user_id, interest) SELECT id, 'Geography' FROM users WHERE email='student7@sensei.com';
INSERT INTO user_interests (user_id, interest) SELECT id, 'Economics' FROM users WHERE email='student7@sensei.com';

-- Student 8: Music only
INSERT INTO user_interests (user_id, interest) SELECT id, 'Guitar' FROM users WHERE email='student8@sensei.com';
INSERT INTO user_interests (user_id, interest) SELECT id, 'Piano' FROM users WHERE email='student8@sensei.com';

-- Student 9: All-rounder
INSERT INTO user_interests (user_id, interest) SELECT id, 'Mathematics' FROM users WHERE email='student9@sensei.com';
INSERT INTO user_interests (user_id, interest) SELECT id, 'English' FROM users WHERE email='student9@sensei.com';
INSERT INTO user_interests (user_id, interest) SELECT id, 'Computer Science' FROM users WHERE email='student9@sensei.com';
INSERT INTO user_interests (user_id, interest) SELECT id, 'Economics' FROM users WHERE email='student9@sensei.com';

-- Student 10: Photography + Art
INSERT INTO user_interests (user_id, interest) SELECT id, 'Photography' FROM users WHERE email='student10@sensei.com';
INSERT INTO user_interests (user_id, interest) SELECT id, 'Drawing' FROM users WHERE email='student10@sensei.com';

-- Student 11: Biology + Yoga (health focused)
INSERT INTO user_interests (user_id, interest) SELECT id, 'Biology' FROM users WHERE email='student11@sensei.com';
INSERT INTO user_interests (user_id, interest) SELECT id, 'Yoga' FROM users WHERE email='student11@sensei.com';

-- Student 12: Economics + Computer Science
INSERT INTO user_interests (user_id, interest) SELECT id, 'Economics' FROM users WHERE email='student12@sensei.com';
INSERT INTO user_interests (user_id, interest) SELECT id, 'Computer Science' FROM users WHERE email='student12@sensei.com';
INSERT INTO user_interests (user_id, interest) SELECT id, 'Mathematics' FROM users WHERE email='student12@sensei.com';

-- Student 13: Languages + Cooking
INSERT INTO user_interests (user_id, interest) SELECT id, 'French' FROM users WHERE email='student13@sensei.com';
INSERT INTO user_interests (user_id, interest) SELECT id, 'Cooking' FROM users WHERE email='student13@sensei.com';
INSERT INTO user_interests (user_id, interest) SELECT id, 'Spanish' FROM users WHERE email='student13@sensei.com';

-- Student 14: STEM heavy
INSERT INTO user_interests (user_id, interest) SELECT id, 'Mathematics' FROM users WHERE email='student14@sensei.com';
INSERT INTO user_interests (user_id, interest) SELECT id, 'Physics' FROM users WHERE email='student14@sensei.com';
INSERT INTO user_interests (user_id, interest) SELECT id, 'Computer Science' FROM users WHERE email='student14@sensei.com';
INSERT INTO user_interests (user_id, interest) SELECT id, 'Chemistry' FROM users WHERE email='student14@sensei.com';

-- Student 15: Music + Language
INSERT INTO user_interests (user_id, interest) SELECT id, 'Piano' FROM users WHERE email='student15@sensei.com';
INSERT INTO user_interests (user_id, interest) SELECT id, 'Hindi' FROM users WHERE email='student15@sensei.com';
INSERT INTO user_interests (user_id, interest) SELECT id, 'English' FROM users WHERE email='student15@sensei.com';

-- Student 16: Geography + History + Drawing
INSERT INTO user_interests (user_id, interest) SELECT id, 'Geography' FROM users WHERE email='student16@sensei.com';
INSERT INTO user_interests (user_id, interest) SELECT id, 'History' FROM users WHERE email='student16@sensei.com';
INSERT INTO user_interests (user_id, interest) SELECT id, 'Drawing' FROM users WHERE email='student16@sensei.com';

-- Student 17: Cooking + Yoga + Photography (lifestyle)
INSERT INTO user_interests (user_id, interest) SELECT id, 'Cooking' FROM users WHERE email='student17@sensei.com';
INSERT INTO user_interests (user_id, interest) SELECT id, 'Yoga' FROM users WHERE email='student17@sensei.com';
INSERT INTO user_interests (user_id, interest) SELECT id, 'Photography' FROM users WHERE email='student17@sensei.com';

-- Student 18: Only Mathematics
INSERT INTO user_interests (user_id, interest) SELECT id, 'Mathematics' FROM users WHERE email='student18@sensei.com';

-- Student 19: Only Guitar
INSERT INTO user_interests (user_id, interest) SELECT id, 'Guitar' FROM users WHERE email='student19@sensei.com';

-- Student 20: Biology + English + Hindi
INSERT INTO user_interests (user_id, interest) SELECT id, 'Biology' FROM users WHERE email='student20@sensei.com';
INSERT INTO user_interests (user_id, interest) SELECT id, 'English' FROM users WHERE email='student20@sensei.com';
INSERT INTO user_interests (user_id, interest) SELECT id, 'Hindi' FROM users WHERE email='student20@sensei.com';

-- Verify
SELECT u.name, GROUP_CONCAT(ui.interest SEPARATOR ', ') as interests 
FROM users u 
JOIN user_interests ui ON u.id = ui.user_id 
WHERE u.role = 'STUDENT' AND u.email LIKE 'student%@sensei.com'
GROUP BY u.name
ORDER BY u.name;
