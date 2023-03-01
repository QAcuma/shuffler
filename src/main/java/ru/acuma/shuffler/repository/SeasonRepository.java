package ru.acuma.shuffler.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.acuma.shuffler.model.entity.Season;

public interface SeasonRepository extends JpaRepository<Season, Long> {
}
