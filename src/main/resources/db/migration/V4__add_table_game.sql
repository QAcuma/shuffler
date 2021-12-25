create table if not exists game
(
    id          bigint generated always as identity primary key,
    event_id    bigint,
    team_a      jsonb,
    team_b      jsonb,
    index       int,
    result      text,
    started_at  timestamptz,
    finished_at timestamptz,

    CONSTRAINT fk_exists_group
        FOREIGN KEY (event_id)
            REFERENCES event (id)
);
