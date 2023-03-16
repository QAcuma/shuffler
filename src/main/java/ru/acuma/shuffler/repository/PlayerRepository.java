package ru.acuma.shuffler.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.acuma.shuffler.model.entity.Player;
import ru.acuma.shufflerlib.model.Discipline;

import java.util.List;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    List<Player> findAllActiveBySeasonIdAndChatIdAndDiscipline(Long seasonId, Long chatId, Discipline discipline);
}
