package ru.mhelper.telegram.actions.answer_services;

import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Сервис печати всех функций
 */
@Service(PrintAllFunctionTextAnswer.SERVICE_NAME)
public class PrintAllFunctionTextAnswer implements TelegramTextAnswer {

    public static final String SERVICE_NAME = "help";

    private final Map<String, TelegramTextAnswer> telegramServices;

    public PrintAllFunctionTextAnswer(Map<String, TelegramTextAnswer> telegramServices) {
        this.telegramServices = telegramServices;
    }

    @Override
    public String action(Long chatId, String text) {
        StringBuilder result = new StringBuilder("All functions:").append(System.lineSeparator());
        telegramServices.forEach((name, service) ->
                result.append(name)
                        .append(": ")
                        .append(service.getInfo())
                        .append(System.lineSeparator()));
        return result.toString();
    }

    @Override
    public String getInfo() {
        return "List of all functions.";
    }
}
