package ru.acuma.shuffler.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.acuma.shuffler.model.entity.RatingHistory;
import ru.acuma.shuffler.model.constant.Discipline;

import java.util.List;
import java.util.Optional;

public interface RatingHistoryRepository extends JpaRepository<RatingHistory, Long> {

    List<RatingHistory> findBySeasonIdAndPlayerIdAndDiscipline(Long seasonId, Long playerId, Discipline discipline);
}
