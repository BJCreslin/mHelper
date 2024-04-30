package ru.thelper.telegram.events

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update

/**
 * The service for handling a callback from the user
 */
@SuppressWarnings("kotlin:S6517")
interface CallbackEvent {

    /**
     * The method for handling a callback from the user
     */
    fun action(update: Update): SendMessage
}