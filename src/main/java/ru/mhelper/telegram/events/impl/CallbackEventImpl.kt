package ru.mhelper.telegram.events.impl

import lombok.extern.slf4j.Slf4j
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import ru.mhelper.telegram.actions.admin_menu.AdminTgMenu
import ru.mhelper.telegram.events.CallbackEvent

@Service
@Slf4j
class CallbackEventImpl(private val adminTgMenu: AdminTgMenu) : CallbackEvent {
    override fun action(update: Update): SendMessage {
        val response = SendMessage()
        val chatId = update.callbackQuery.message.chatId
        response.chatId = chatId.toString()
        val text = update.callbackQuery.data
        val sendText: String = adminTgMenu.getMenuMessageText(chatId, text)
        response.text = sendText
        return response
    }
}