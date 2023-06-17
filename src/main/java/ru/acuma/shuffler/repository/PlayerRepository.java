package ru.acuma.shuffler.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.acuma.shuffler.model.entity.Player;

import java.util.Optional;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    Optional<Player> findByUserIdAndChatId(Long id, Long chatId);
}
