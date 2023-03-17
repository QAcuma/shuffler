package ru.acuma.shuffler.model.entity;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import ru.acuma.shuffler.model.enums.EventState;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;
import java.time.OffsetDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@ToString

@Entity
@Table(name = "event")
@DynamicUpdate
@DynamicInsert

@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Event implements Serializable {
    @Serial
    private static final long serialVersionUID = 7613068652225005723L;
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "chat_id", nullable = false)
    private GroupInfo chat;

    @NotNull
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "season_id", nullable = false)
    private Season season;

    @Size(max = 32)
    @NotNull
    @Column(name = "state", nullable = false, length = 32)
    @Enumerated(EnumType.STRING)
    private EventState state;

    @Size(max = 32)
    @NotNull
    @Column(name = "discipline", nullable = false, length = 32)
    private String discipline;

    @NotNull
    @Column(name = "started_at", nullable = false)
    private OffsetDateTime startedAt;

    @Column(name = "finished_at")
    private OffsetDateTime finishedAt;

}
