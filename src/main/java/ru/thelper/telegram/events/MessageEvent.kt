package ru.thelper.telegram.events

import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update

/**
 * The service for processing a text command from the user
 */
@SuppressWarnings("kotlin:S6517")
interface MessageEvent {

    /**
     * The method for handling a text command from the user
     */
    fun action(update: Update): SendMessage
}