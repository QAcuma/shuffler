create table if not exists season
(
    id          bigint generated always as identity primary key,
    started_at  timestamptz not null,
    finished_at timestamptz,
    name        varchar(64)
);