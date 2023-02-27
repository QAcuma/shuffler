create table if not exists player
(
    id         bigint generated always as identity primary key,
    chat_id    bigint not null,
    user_id    bigint not null,
    season_id  bigint not null,

    CONSTRAINT fk_exists_chat
        FOREIGN KEY (chat_id)
            REFERENCES group_info (chat_id),

    CONSTRAINT fk_exists_user
        FOREIGN KEY (user_id)
            REFERENCES user_info (telegram_id),

    CONSTRAINT fk_exists_season
        FOREIGN KEY (season_id)
            REFERENCES season (id)
);
