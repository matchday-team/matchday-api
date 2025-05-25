ALTER TABLE match_event
    ADD COLUMN parent_id BIGINT;

ALTER TABLE match_event
    ADD CONSTRAINT fk_match_event_parent
        FOREIGN KEY (parent_id)
            REFERENCES match_event(id)
            ON DELETE CASCADE;