package ru.acuma.shuffler.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.acuma.shuffler.model.entity.Player;
import ru.acuma.shufflerlib.model.Discipline;

import java.util.List;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    @Query(value = """
        from Player
        join 
        """)
    List<Player> findAllActiveBySeasonIdAndChatIdAndDiscipline(Long seasonId, Long chatId, Discipline discipline);
}
