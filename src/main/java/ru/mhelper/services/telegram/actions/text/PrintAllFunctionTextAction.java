package ru.mhelper.services.telegram.actions.text;

import org.springframework.stereotype.Service;

import java.util.Map;

@Service("help")
public class PrintAllFunctionTextAction implements TelegramTextAction {
    private final Map<String, TelegramTextAction> telegramServices;

    public PrintAllFunctionTextAction(Map<String, TelegramTextAction> telegramServices) {
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
