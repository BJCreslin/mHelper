package ru.mhelper.user.service

import org.springframework.stereotype.Service
import ru.mhelper.models.users.User
import ru.mhelper.repository.UserRepository
import java.util.function.Supplier

@Service
class UserServiceImpl(
    private val userRepository: UserRepository
) : UserService {

    /**
     * Просим у репозитория пользователя. Если такого пользователя нет - создаем нового и возвращаем его.
     * Как раз на случай нового пользователя мы и сделали конструктор с одним параметром в классе User
     */
    override fun getUserByChatId(chatId: Long): User {

        //
        val user: User = userRepository.findByTelegramUserId(chatId)
            .orElseGet(Supplier<User> {
                userRepository.save<User>(
                    User(chatId)
                )
            })
        return user
    }
}