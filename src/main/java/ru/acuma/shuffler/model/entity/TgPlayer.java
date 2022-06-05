package ru.acuma.shuffler.model.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.acuma.shuffler.tables.pojos.UserInfo;

import java.util.Optional;

@Getter
@Setter
@Accessors(chain = true)
public class TgPlayer extends UserInfo {

    private Long id;

    private Long chatId;

    private int score;

    public String getName() {
        return super.getFirstName() +
                " " +
                Optional.ofNullable(super.getLastName()).orElse("Doe") +
                score;
    }

    public void plusRating(long value) {
        score += value;
    }

    public void minusRating(long value) {
        score -= value;
    }

}
