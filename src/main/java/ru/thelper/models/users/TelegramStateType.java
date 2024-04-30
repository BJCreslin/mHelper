package ru.thelper.models.users;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TelegramStateType {

    NO_STATEMENT("NO_STATEMENT"),

    NEW_TELEGRAM_USER("NEW_TELEGRAM_USER"),

    GETTING_CODE("GETTING_CODE");

    public final String statement;

}
