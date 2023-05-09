package ru.mhelper.telegram.events

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update

/**
 * Сервис получения коллбека
 */
interface CallbackEvent {
    fun action(update: Update): SendMessage
}