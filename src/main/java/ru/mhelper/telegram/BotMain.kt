package ru.mhelper.telegram

import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import ru.mhelper.events_application_manager.TelegramEvent
import ru.mhelper.telegram.cfg.BotConfiguration
import ru.mhelper.telegram.events.CallbackEvent
import ru.mhelper.telegram.events.MessageEvent
import ru.mhelper.telegram.exception.TelegramBotServiceException

/**
 * Main class for telegram bot
 */
@Component
class BotMain(

    private val botConfiguration: BotConfiguration,
    private val messageEvent: MessageEvent,
    private val callbackEvent: CallbackEvent,
    private val applicationEventPublisher: ApplicationEventPublisher
) :
    TelegramLongPollingBot(botConfiguration.token) {

    private val logger = LoggerFactory.getLogger(javaClass)

    override fun getBotUsername(): String = botConfiguration.name

    override fun onUpdateReceived(update: Update?) {
        if (update != null) {
            val response: SendMessage? = if (update.hasMessage()) {
                logger.info("received new message from ${update.message.chatId}")
                messageEvent.action(update)
            } else if (update.hasCallbackQuery()) {
                logger.info("received new callback from ${update.callbackQuery.message.chatId}")
                callbackEvent.action(update)
            } else {
                logger.warn("Unknown telegram message type received. $update")
                null
            }
            try {
                if (response != null) {
                    execute<Message, SendMessage>(response)
                }
            } catch (ex: TelegramApiException) {
                val eMessage =
                    response?.let { String.format(TelegramBotServiceException.ERROR_SEND_MESSAGE, it.chatId, it.text) }
                throw TelegramBotServiceException(eMessage, ex)
            }
        }
    }

    @PostConstruct
    fun start() {
        val telegramEvent = TelegramEvent(this, "Telegram bot started. botName ${botConfiguration.name}")
        applicationEventPublisher.publishEvent(telegramEvent)
    }
}