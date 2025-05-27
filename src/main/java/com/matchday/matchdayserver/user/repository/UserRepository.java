package com.matchday.matchdayserver.user.repository;

import com.matchday.matchdayserver.user.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsById(Long id);
    User findBySocialId(String socialId);
}
