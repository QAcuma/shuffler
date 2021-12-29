create table if not exists game
(
    id          bigint generated always as identity primary key,
    event_id    bigint      not null,
    index       int         not null,
    started_at  timestamptz not null,
    finished_at timestamptz,

    CONSTRAINT fk_exists_event
        FOREIGN KEY (event_id)
            REFERENCES event (id)
);
