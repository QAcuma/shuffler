package ru.acuma.shuffler.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.acuma.shuffler.model.constant.Discipline;
import ru.acuma.shuffler.model.entity.RatingHistory;

import java.util.List;

public interface RatingHistoryRepository extends JpaRepository<RatingHistory, Long> {

    List<RatingHistory> findBySeasonIdAndPlayerIdAndDiscipline(Long seasonId, Long playerId, Discipline discipline);
}
