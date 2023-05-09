package ru.mhelper.user.service

import ru.mhelper.models.users.User

interface UserService {

    fun getUserByChatId(chatId: Long): User
}