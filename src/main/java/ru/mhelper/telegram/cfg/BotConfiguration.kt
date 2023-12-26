package ru.mhelper.telegram.cfg

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

/**
 * The component with the Telegram bot's properties
 */
@Component
@ConfigurationProperties("bot")
class BotConfiguration {

    /**
     * Bot`s name
     */
    lateinit var name: String

    /**
     * Bot`s token
     */
    lateinit var token: String
}



