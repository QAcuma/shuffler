package ru.acuma.shuffler.model.enums;
// Перенести в апп проперти

public final class Values {
    public static final int TIMEOUT = 3;
    public static final int GAME_PLAYERS_COUNT = 4;
    public static final int CANCELLED_MESSAGE_TIMEOUT = 300;
    public static final int DEFAULT_RATING = 1000;
    public static final int BASE_RATING_CHANGE = 25;
    /**
     * Повышение -> снижение штрафа за разницу в рейтинге
     * ПОнижение -> увеличение штрафа за разницу в рейтинге
     */
    public static final int RATING_REFERENCE = 750;
}
