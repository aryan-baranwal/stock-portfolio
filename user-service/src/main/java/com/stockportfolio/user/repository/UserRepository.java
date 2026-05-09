package com.stockportfolio.user.repository;

import com.stockportfolio.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Page<User> findByDeletedFalse(Pageable pageable);

    Optional<User> findByIdAndDeletedFalse(Long id);
}