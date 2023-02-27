create table if not exists rating
(
    id         bigint generated always as identity primary key,
    player_id  bigint  not null,
    season_id  bigint  not null,
    discipline varchar(32),
    rating     integer not null,

    CONSTRAINT fk_exists_player
        FOREIGN KEY (player_id)
            REFERENCES player (id),

    CONSTRAINT fk_exists_season
        FOREIGN KEY (season_id)
            REFERENCES season (id)
);
