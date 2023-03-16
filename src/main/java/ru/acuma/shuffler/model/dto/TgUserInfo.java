package ru.acuma.shuffler.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import ru.acuma.shuffler.model.entity.UserInfo;

import java.io.Serializable;
import java.time.OffsetDateTime;

/**
 * A DTO for the {@link UserInfo} entity
 */
@Data
@SuperBuilder
@NoArgsConstructor
@Accessors(chain = true)
public class TgUserInfo implements Serializable {
    private Long telegramId;
    private String languageCode;
    private Boolean isBot;
    private String firstName;
    private String lastName;
    private String userName;
    private Boolean isBlocked;
    private OffsetDateTime lastMessageAt;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private OffsetDateTime deletedAt;
    private Byte[] mediaBlob;
    private String mediaId;
}
