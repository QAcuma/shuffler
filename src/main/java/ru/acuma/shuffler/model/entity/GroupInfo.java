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
import lombok.experimental.Accessors;

import javax.validation.constraints.Size;
import java.io.Serial;
import java.io.Serializable;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@ToString
@Entity
@Table(name = "group_info")
public class GroupInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = -4010360346875311472L;
    @Id
    @Column(name = "chat_id", nullable = false)
    private Long id;

    @Column(name = "title", length = Integer.MAX_VALUE)
    private String title;

    @Column(name = "is_blocked")
    private Boolean isBlocked;

    @Size(max = 16)
    @Column(name = "name", length = 16)
    private String name;

}
