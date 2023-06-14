alter table user_info
    drop column if exists last_message_at;
alter table user_info
    drop column if exists media_blob;
alter table user_info
    drop column if exists is_bot;
