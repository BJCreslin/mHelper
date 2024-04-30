package ru.thelper.services.models_sevices.user

import ru.thelper.models.users.User

interface UserService {

    fun createNewTelegramUser(telegramId: Long?): User?

    fun existsByTelegramUserId(telegramId: Long?): Boolean

    fun existsByUsername(userName: String?): Boolean

    fun existsByEmail(email: String?): Boolean

    fun getUserByTelegramId(telegramId: Long?): User

    fun getOrNewUserByTelegramId(telegramId: Long?): User

    fun getUserByUserName(userName: String?): User

    fun save(user: User?): User

    /**
     * Метод проверки корректности пользователя
     */
    fun isUserCorrect(user: User?): CheckUserResult

}