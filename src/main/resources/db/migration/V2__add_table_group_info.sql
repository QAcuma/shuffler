create table if not exists group_info
(
    id          bigint generated always as identity primary key,
    telegram_id bigint not null,
    title       text,
    members     jsonb
);
