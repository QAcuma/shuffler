create table if not exists user_info
(
    id                         bigint generated always as identity primary key,
    telegram_id                bigint      not null,
    language_code              text,
    is_bot                     boolean,
    first_name                 text,
    last_name                  text,
    user_name                  text        not null,
    is_blocked                 boolean     not null,
    can_join_groups            boolean,
    can_read_all_group_message boolean,
    support_inline_queries     boolean,
    last_message_at            timestamptz,
    created_at                 timestamptz not null,
    updated_at                 timestamptz,
    deleted_at                 timestamptz
);
