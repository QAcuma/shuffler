package ru.acuma.shuffler.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.acuma.shuffler.model.entity.GroupInfo;

public interface GroupInfoRepository extends JpaRepository<GroupInfo, Long> {
}
