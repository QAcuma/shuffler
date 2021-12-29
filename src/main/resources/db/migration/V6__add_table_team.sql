create table if not exists team
(
    id          bigint generated always as identity primary key,
    game_id     bigint      not null,
    messages    jsonb,
    players     jsonb,
    started_at  timestamptz not null,
    finished_at timestamptz,

    CONSTRAINT fk_exists_game
        FOREIGN KEY (game_id)
            REFERENCES game (id)
);
