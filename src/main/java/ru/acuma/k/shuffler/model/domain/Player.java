package ru.acuma.k.shuffler.model.domain;

import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.Optional;

@Getter
@Setter
public class Player extends User {

    private Long rating;

    private Long chatId;

    public String getName() {
        StringBuilder builder = new StringBuilder();
        builder.append(super.getFirstName())
                .append(" ")
                .append(Optional.ofNullable(super.getLastName()).orElse("Doe"));
        return builder.toString();
    }

}
