package ru.acuma.shuffler.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.acuma.shuffler.model.entity.Team;

public interface TeamRepository extends JpaRepository<Team, Long> {
}
