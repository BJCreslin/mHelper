package ru.mhelper.services.telegram;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.LongPollingBot;

//@Configuration
public class BotConfiguration {

//    @Bean
//    public LongPollingBot telegramBot(DefaultBotOptions botOptions) {
//        return new Bot(botOptions);
//    }
//
//    @Bean
//    public DefaultBotOptions botOptions(){
//        return new DefaultBotOptions();
//    }
//
//    @Bean
//    public TelegramBotsApi telegramBotsApi(LongPollingBot telegramBot) throws TelegramApiException {
//        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
//        telegramBotsApi.registerBot(telegramBot);
//        return telegramBotsApi;
//    }
}
