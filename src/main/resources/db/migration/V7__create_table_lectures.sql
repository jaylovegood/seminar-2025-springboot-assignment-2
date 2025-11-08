CREATE TABLE IF NOT EXISTS lectures
(
    id         BIGINT AUTO_INCREMENT
        PRIMARY KEY,
    academic_year INT NOT NULL,
    semester VARCHAR(10) NOT NULL,
    lecture_type VARCHAR(10) NOT NULL,
    college VARCHAR(30) NOT NULL,
    department VARCHAR(30) NOT NULL,
    target VARCHAR(20) NOT NULL,
    grade INT NOT NULL,
    course_number VARCHAR(20) NOT NULL,
    lecture_number VARCHAR(20) NOT NULL,
    title VARCHAR(50) NOT NULL,
    subtitle VARCHAR(50) NOT NULL,
    credit INT NOT NULL,
    lecturer VARCHAR(50) NOT NULL
);
