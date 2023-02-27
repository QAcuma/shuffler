alter table group_info
    add column name varchar(16),
    add constraint unique_name
    unique (name);