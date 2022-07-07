package ru.mhelper.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mhelper.models.users.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String userName);

    Optional<User> findByTelegramUserId(Long telegramUserId);

    boolean existsByUsername(String userName);

    boolean existsByTelegramUserId(Long telegramUserId);

    boolean existsByEmail(String email);

    List<User> findUsersByProcurementsIsNotNullAndTelegramUserIdIsNotNull();

}
