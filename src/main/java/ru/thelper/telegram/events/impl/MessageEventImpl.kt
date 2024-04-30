package ru.thelper.telegram.events.impl

import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import ru.thelper.telegram.actions.admin_menu.AdminTgMenu
import ru.thelper.telegram.actions.answer_services.PrintAllFunctionTextAnswer
import ru.thelper.telegram.actions.answer_services.TelegramTextAnswer
import ru.thelper.telegram.events.MessageEvent

@Service
class MessageEventImpl(
    private val textActions: Map<String, TelegramTextAnswer>,
    private val adminTgMenu: AdminTgMenu
) : MessageEvent {

    override fun action(update: Update): SendMessage {
        val message = update.message
        var response = SendMessage()
        val chatId = message.chatId
        response.chatId = chatId.toString()
        var text = message.text.lowercase()
        if (textActions.containsKey(text)) {
            text = textActions.getValue(text).action(chatId, text)
        } else if (text.equals("menu", ignoreCase = true)) {
            response = adminTgMenu.sendInlineKeyBoardMessage(chatId)
        } else {
            textActions.getValue(PrintAllFunctionTextAnswer.SERVICE_NAME).action(chatId, text)
        }
        response.text = text
        return response
    }
}