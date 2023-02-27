alter table rating_history
    add column season_id bigint,
    add constraint fk_exists_season
        foreign key (season_id)
            references season (id);

update rating_history
set season_id = 1
where season_id is null;

alter table rating_history
    alter column season_id set not null;