package ru.zhelper.zhelper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.zhelper.zhelper.models.users.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String userName);

    Optional<User> findByTelegramUserId(String telegramUserId);

    boolean existsByUsername(String userName);

    boolean existsByTelegramUserId(String telegramUserId);

    boolean existsByEmail(String email);
}
