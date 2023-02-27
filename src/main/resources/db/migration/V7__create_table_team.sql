create table if not exists team
(
    id        bigint generated always as identity primary key,
    game_id   bigint not null,
    is_winner boolean,

    CONSTRAINT fk_exists_game
        FOREIGN KEY (game_id)
            REFERENCES game (id)
);
