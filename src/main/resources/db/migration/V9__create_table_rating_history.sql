create table if not exists rating_history
(
    id        bigint generated always as identity primary key,
    game_id   bigint  not null,
    player_id bigint  not null,
    change    integer not null,

    CONSTRAINT fk_exists_game
        FOREIGN KEY (game_id)
            REFERENCES game (id),

    CONSTRAINT fk_exists_player
        FOREIGN KEY (player_id)
            REFERENCES player (id)
);
