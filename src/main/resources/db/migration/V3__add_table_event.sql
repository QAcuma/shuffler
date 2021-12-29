create table if not exists event
(
    id          bigint generated always as identity primary key,
    chat_id     bigint      not null,
    messages    jsonb,
    players     jsonb,
    status      text        not null,
    started_at  timestamptz not null,
    finished_at timestamptz,

    CONSTRAINT fk_exists_chat
        FOREIGN KEY (chat_id)
            REFERENCES group_info (chat_id)
);
