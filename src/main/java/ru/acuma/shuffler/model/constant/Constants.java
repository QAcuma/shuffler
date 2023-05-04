package ru.acuma.shuffler.model.constant;

//TODO: Перенести в апп проперти
public final class Constants {
    public static final int DISABLED_BUTTON_TIMEOUT = 3;
    public static final int GAME_PLAYERS_COUNT = 4;
    public static final Integer CANCELLED_MESSAGE_TTL_BEFORE_DELETE = 30;
    public static final int BASE_RATING = 1000;
    public static final int BASE_RATING_CHANGE = 25;
    /**
     * Повышение -> снижение штрафа за разницу в рейтинге
     * ПОнижение -> увеличение штрафа за разницу в рейтинге
     */
    public static final int RATING_REFERENCE = 625;
}
