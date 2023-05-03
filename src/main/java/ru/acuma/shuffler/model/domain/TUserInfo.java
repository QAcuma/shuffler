package ru.acuma.shuffler.model.domain;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Accessors;
import ru.acuma.shuffler.model.entity.UserInfo;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * A DTO for the {@link UserInfo} entity
 */
@Data
@Builder
@Accessors(chain = true)
public class TUserInfo implements Serializable {
    private Long userId;
    private String languageCode;
    private Boolean isBot;
    private String firstName;
    private String lastName;
    private String userName;
    private Boolean isActive;
    private LocalDateTime lastMessageAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
