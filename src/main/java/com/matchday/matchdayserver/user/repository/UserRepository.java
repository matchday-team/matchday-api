package com.matchday.matchdayserver.user.repository;

import com.matchday.matchdayserver.user.model.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByName(String name);
}
