package ru.acuma.shuffler.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.acuma.shuffler.model.entity.GroupInfo;
import ru.acuma.shuffler.model.projection.GroupInfoAuth;
import ru.acuma.shuffler.model.projection.UserInfoAuth;

import java.util.List;
import java.util.Optional;

public interface GroupInfoRepository extends JpaRepository<GroupInfo, Long> {
    List<GroupInfo> findAllByIsActiveTrue();

    Optional<GroupInfoAuth> findForAuthById(Long userId);
}
