package ru.thelper.telegram.handlers;

import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import ru.thelper.models.users.TelegramStateType;
import ru.thelper.models.users.User;

import java.util.List;
import java.io.Serializable;

public interface Handler {

    // основной метод, который будет обрабатывать действия пользователя
    List<PartialBotApiMethod<? extends Serializable>> handle(User user, String message);

    // метод, который позволяет узнать, можем ли мы обработать текущий State у пользователя
    TelegramStateType operatedBotState();

    // метод, который позволяет узнать, какие команды CallBackQuery мы можем обработать в этом классе
    List<String> operatedCallBackQuery();
}
