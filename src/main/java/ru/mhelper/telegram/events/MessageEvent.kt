package ru.mhelper.telegram.events

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update

/**
 * Сервис получения текста
 */
interface MessageEvent {
    fun action(update: Update): SendMessage
}