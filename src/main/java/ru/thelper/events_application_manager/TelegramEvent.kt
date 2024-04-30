package ru.thelper.events_application_manager

import org.springframework.context.ApplicationEvent

/**
 * Placeholder to store the telegram`s event data
 */
class TelegramEvent(sourceClass: Any?, val message: String) : ApplicationEvent(sourceClass!!)