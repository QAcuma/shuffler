package ru.acuma.shuffler.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.acuma.shuffler.model.entity.UserInfo;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * A DTO for the {@link UserInfo} entity
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TgUserInfo implements Serializable {
    private Long telegramId;
    private String languageCode;
    private Boolean isBot;
    private String firstName;
    private String lastName;
    @NotNull
    private String userName;
    @NotNull
    private Boolean isBlocked = false;
    private OffsetDateTime lastMessageAt;
    @NotNull
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private OffsetDateTime deletedAt;
    private byte[] mediaBlob;
    private String mediaId;
}
