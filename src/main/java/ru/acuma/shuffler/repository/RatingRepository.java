package ru.acuma.shuffler.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.acuma.shuffler.model.constant.Discipline;
import ru.acuma.shuffler.model.entity.GroupInfo;
import ru.acuma.shuffler.model.entity.Player;
import ru.acuma.shuffler.model.entity.Rating;
import ru.acuma.shuffler.model.entity.Season;

import java.util.List;
import java.util.Optional;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    Optional<Rating> findBySeasonIdAndPlayerAndDiscipline(Long seasonId, Player player, Discipline discipline);

    @Query("""
        select r
        from Rating r
        join fetch r.player p
        where r.season = :season
        and r.discipline = :discipline
        and p.chat = :groupInfo
        """)
    List<Rating> findALlSeasonRatings(Season season, GroupInfo groupInfo, Discipline discipline);
}
