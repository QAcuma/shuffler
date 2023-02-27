create table if not exists team_player
(
    id        bigint generated always as identity primary key,
    player_id bigint not null,
    team_id   bigint not null,

    CONSTRAINT fk_exists_player
        FOREIGN KEY (player_id)
            REFERENCES player (id),

    CONSTRAINT fk_exists_team
        FOREIGN KEY (team_id)
            REFERENCES team (id)
);
