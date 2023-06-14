alter table group_info
    rename column chat_id to id;

alter table user_info
    rename column telegram_id to id;
