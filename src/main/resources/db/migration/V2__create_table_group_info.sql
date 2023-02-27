create table if not exists group_info
(
    chat_id    bigint primary key,
    title      text,
    is_blocked boolean
);
