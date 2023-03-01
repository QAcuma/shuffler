package ru.acuma.shuffler.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.acuma.shuffler.model.entity.Player;

public interface PlayerRepository extends JpaRepository<Player, Long> {
}
