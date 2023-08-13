package ru.mhelper.services.models_sevices.user

import ru.mhelper.models.users.User

interface UserService {

    fun createNewTelegramUser(telegramId: Long?): User?

    fun existsByTelegramUserId(telegramId: Long?): Boolean

    fun existsByUsername(userName: String?): Boolean

    fun existsByEmail(email: String?): Boolean

    fun getUserByTelegramId(telegramId: Long?): User

    fun getOrNewUserByTelegramId(telegramId: Long?): User

    fun getUserByUserName(userName: String?): User

    fun save(user: User?): User

}