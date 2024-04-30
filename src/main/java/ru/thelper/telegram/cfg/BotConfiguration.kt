package ru.thelper.telegram.cfg

import lombok.Data
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component

/**
 * The component with the Telegram bot's properties
 */
@Component
@ConfigurationProperties("bot")
@Configuration
@Data
open class BotConfiguration {
    /**
     * Bot`s name
     */
    lateinit var name: String;

    /**
     * Bot`s token
     */
    lateinit var token: String

    /**
     * Bot`s time
     */
    var time = 5

    /**
     * Bot`s max_attempts
     */
    var maxAttempts = 100
}