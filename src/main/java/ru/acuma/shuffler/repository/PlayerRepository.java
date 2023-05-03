package ru.acuma.shuffler.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.acuma.shuffler.model.entity.Player;
import ru.acuma.shuffler.model.constant.Discipline;

import java.util.List;
import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    @Query(value = """
        from Player
        join GroupInfo
        """)
    List<Player> findAllActiveBySeasonIdAndChatIdAndDiscipline(Long seasonId, Long chatId, Discipline discipline);

    Optional<Player> findByUserIdAndChatId(Long id, Long chatId);

    boolean existsByIdAndChatId(Long userId, Long chatId);
}
