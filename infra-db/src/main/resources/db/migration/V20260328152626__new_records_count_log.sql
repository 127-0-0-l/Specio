DELETE FROM new_records_count_log;

ALTER TABLE new_records_count_log
    DROP COLUMN date_time,
    ADD COLUMN first_week_day DATE NOT NULL,
    ADD COLUMN week_seconds_from INT NOT NULL,
    ADD COLUMN week_seconds_to INT NOT NULL,
    ADD CONSTRAINT chk_nrcl_seconds CHECK (week_seconds_from <= week_seconds_to)