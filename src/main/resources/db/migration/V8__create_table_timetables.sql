CREATE TABLE IF NOT EXISTS timetables
(
    id              BIGINT AUTO_INCREMENT
        PRIMARY KEY,
    user_id         BIGINT NOT NULL,
    timetable_name  VARCHAR(255) NOT NULL UNIQUE,
    year            INT NOT NULL,
    semester        VARCHAR(20)  NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
