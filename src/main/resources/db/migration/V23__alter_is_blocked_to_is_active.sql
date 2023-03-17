alter table user_info
    rename column is_blocked
        to is_active;

update user_info
set is_active = not is_active;

alter table group_info
    rename column is_blocked
        to is_active;

update group_info
set is_active = not is_active;
