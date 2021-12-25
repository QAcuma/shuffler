create table if not exists event
(
    id          bigint generated always as identity primary key,
    group_id    bigint,
    members     jsonb,
    games       int,
    started_at  timestamptz not null,
    finished_at timestamptz
);
