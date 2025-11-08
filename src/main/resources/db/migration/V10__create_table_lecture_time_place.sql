CREATE TABLE IF NOT EXISTS lecture_time_place
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    lecture_id BIGINT NOT NULL,
    day_of_week VARCHAR(10),
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    location VARCHAR(50) NOT NULL,
    CONSTRAINT lecture_time_place__fk__lecture_id
        FOREIGN KEY (lecture_id) REFERENCES lectures (id) ON DELETE CASCADE
);