package ru.acuma.shuffler.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.acuma.shuffler.model.entity.Game;

public interface GameRepository extends JpaRepository<Game, Long> {
}
