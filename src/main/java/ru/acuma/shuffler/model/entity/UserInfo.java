package ru.acuma.shuffler.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "user_info")
public class UserInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = 5629283560177967494L;
    @Id
    @Column(name = "telegram_id", nullable = false)
    private Long id;

    @Column(name = "language_code", length = Integer.MAX_VALUE)
    private String languageCode;

    @Column(name = "is_bot")
    private Boolean isBot;

    @Column(name = "first_name", length = Integer.MAX_VALUE)
    private String firstName;

    @Column(name = "last_name", length = Integer.MAX_VALUE)
    private String lastName;

    @NotNull
    @Column(name = "user_name", nullable = false, length = Integer.MAX_VALUE)
    private String userName;

    @NotNull
    @Column(name = "is_blocked", nullable = false)
    private Boolean isBlocked = false;

    @Column(name = "last_message_at")
    private OffsetDateTime lastMessageAt;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;

    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;

    @Column(name = "media_blob")
    private byte[] mediaBlob;

    @Column(name = "media_id", length = Integer.MAX_VALUE)
    private String mediaId;

}
