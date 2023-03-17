package ru.acuma.shuffler.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.acuma.shuffler.model.entity.GroupInfo;

import java.util.List;

public interface GroupInfoRepository extends JpaRepository<GroupInfo, Long> {
    List<GroupInfo> findAllByIsActiveTrue();
}
