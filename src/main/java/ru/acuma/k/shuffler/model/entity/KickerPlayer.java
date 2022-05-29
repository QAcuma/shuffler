package ru.acuma.k.shuffler.model.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import ru.acuma.k.shuffler.tables.pojos.UserInfo;

import java.util.Optional;

@Getter
@Setter
@Accessors(chain = true)
public class KickerPlayer extends UserInfo {

    private Long id;

    private Long chatId;

    private int rating;

    public String getName() {
        StringBuilder builder = new StringBuilder();
        builder.append(super.getFirstName())
                .append(" ")
                .append(Optional.ofNullable(super.getLastName()).orElse("Doe"))
                .append(rating);
        return builder.toString();
    }

    public void plusRating(long value) {
        rating += value;
    }

    public void minusRating(long value) {
        rating -= value;
    }

}
