alter table rating
    add column multiplier decimal
        not null
        default 1;

alter table rating
    drop column is_calibrated;

alter table rating_history
    drop column is_calibrated;


