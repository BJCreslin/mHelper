package ru.mhelper.telegram.cfg

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties("bot")
class BotConfiguration {
    lateinit var name: String
    lateinit var token: String
}



