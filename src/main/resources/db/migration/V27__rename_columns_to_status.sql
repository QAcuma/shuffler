alter table event
    rename column state to status;
alter table game
    rename column state to status;
