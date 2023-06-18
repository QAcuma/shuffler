package ru.acuma.shuffler.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import ru.acuma.shuffler.service.storage.Storable;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Optional;

@Slf4j
@Data
@Builder
@Accessors(chain = true)
@AllArgsConstructor
public class TEventPlayer implements Serializable, Storable {
    private Long id;
    private Long chatId;
    @EqualsAndHashCode.Exclude
    private TEventPlayer lastGamePlayer;
    private TRating ratingContext;
    private TEventContext eventContext;
    private TUserInfo userInfo;

    public void putGame(final Long gameId, final Integer scoreChange) {
        ratingContext.getGameHistory().put(gameId, ratingContext.getMultiplier().multiply(BigDecimal.valueOf(scoreChange)).intValue());
    }

    public void applyRating(Integer change) {
        log.info("Score change for player {}", getFormatName());
        ratingContext.applyScore(change);
    }

    public Long getUserId() {
        return userInfo.getUserId();
    }

    public Integer getScore() {
        return ratingContext.getScore();
    }

    public Boolean isLeft() {
        return eventContext.getLeft();
    }

    public String getNameWithRating() {
        return userInfo.getFirstName() +
            " " +
            Optional.ofNullable(userInfo.getLastName()).orElse("") +
            getScore();
    }

    public void increaseGameCount() {
        eventContext.increaseGameCount();
    }

    public String getName() {
        return getUserInfo().getFirstName() +
            " " +
            getFormatName();
    }

    private String getFormatName() {
        String lastName = "";
        if (getUserInfo().getLastName() != null) {
            lastName = getUserInfo().getLastName().charAt(0) + ".";
        }
        StringUtils.capitalize(lastName);

        return lastName;
    }

    public String getLobbyName() {
        return strikethroughBegin() +
            getName() +
            strikethroughEnd() +
            " " +
            getScore() +
            (ratingContext.getEventScoreChange() != 0 ? getSessionRatingToString() : "");
    }

    private String strikethroughBegin() {
        return Boolean.TRUE.equals(eventContext.getLeft()) ? "<s>" : "";
    }

    private String strikethroughEnd() {
        return Boolean.TRUE.equals(eventContext.getLeft()) ? "</s>" : "";
    }

    String getSessionRatingToString() {
        return ratingContext.getEventScoreChange() > 0
               ? " (+" + ratingContext.getEventScoreChange() + ")"
               : " (" + ratingContext.getEventScoreChange() + ")";
    }
}
