package ru.acuma.shuffler.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.acuma.shuffler.model.entity.UserInfo;
import ru.acuma.shuffler.model.projection.UserInfoAuth;

import java.util.List;
import java.util.Optional;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {

    List<UserInfo> findAllByIsActiveTrue();

    Optional<UserInfoAuth> findForAuthById(Long userId);
}
