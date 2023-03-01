package ru.acuma.shuffler.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.acuma.shuffler.model.entity.Rating;

public interface RatingRepository extends JpaRepository<Rating, Long> {
}
