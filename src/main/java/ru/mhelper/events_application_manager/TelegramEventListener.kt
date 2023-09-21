package ru.mhelper.events_application_manager

import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component

/**
 * Telegram`s event listener
 */
@Component
class TelegramEventListener : ApplicationListener<TelegramEvent> {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun onApplicationEvent(event: TelegramEvent) {
        logger.info(event.message)
    }
}