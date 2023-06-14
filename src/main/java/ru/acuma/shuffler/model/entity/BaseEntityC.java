package ru.acuma.shuffler.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import ru.acuma.shuffler.util.TimeMachine;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@ToString(callSuper = true)
@MappedSuperclass
public abstract class BaseEntityC extends BaseEntity {

    @NotNull
    @Column(name = "startedAt", nullable = false)
    private LocalDateTime startedAt;

    @PrePersist
    void prePersist() {
        this.startedAt = TimeMachine.localDateTimeNow();
    }
}
