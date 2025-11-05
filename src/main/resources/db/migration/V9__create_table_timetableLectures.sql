CREATE TABLE IF NOT EXISTS timetableLectures
(
    id              BIGINT AUTO_INCREMENT
        PRIMARY KEY,
    timetable_id    BIGINT NOT NULL,
    lecture_id      BIGINT NOT NULL,

    UNIQUE (timetable_id, lecture_id),
    CONSTRAINT timetableLectures__fk__timetable_id
        FOREIGN KEY (timetable_id) REFERENCES timetables(id) ON DELETE CASCADE,
    CONSTRAINT timetableLectures__fk__lecture_id
    FOREIGN KEY (lecture_id) REFERENCES lectures(id) ON DELETE CASCADE
);
