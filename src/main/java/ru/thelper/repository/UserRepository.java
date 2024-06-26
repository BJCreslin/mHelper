package ru.thelper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.thelper.models.users.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String userName);

    Optional<User> findByTelegramUserId(Long telegramUserId);

    boolean existsByUsername(String userName);

    boolean existsByTelegramUserId(Long telegramUserId);

    boolean existsByEmail(String email);
}
