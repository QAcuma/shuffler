package ru.acuma.shuffler.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.acuma.shuffler.model.constant.Discipline;
import ru.acuma.shuffler.model.entity.Player;
import ru.acuma.shuffler.model.entity.Rating;

import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    Optional<Rating> findBySeasonIdAndPlayerAndDiscipline(Long seasonId, Player player, Discipline discipline);
}
