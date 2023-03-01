package ru.acuma.shuffler.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.acuma.shuffler.model.entity.UserInfo;

public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
}
