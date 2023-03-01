package ru.acuma.shuffler.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.acuma.shuffler.model.entity.TeamPlayer;

public interface TeamPlayerRepository extends JpaRepository<TeamPlayer, Long> {
}
