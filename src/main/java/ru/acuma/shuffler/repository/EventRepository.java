package ru.acuma.shuffler.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.acuma.shuffler.model.entity.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
}
