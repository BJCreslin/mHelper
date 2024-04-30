package ru.thelper.models.users;

public enum TelegramStateType {

    NO_STATEMENT("NO_STATEMENT"),

    NEW_TELEGRAM_USER("NEW_TELEGRAM_USER"),

    GETTING_CODE("GETTING_CODE");

    public final String statement;

    public String getStatement() {
        return statement;
    }

    TelegramStateType(String statement) {
        this.statement = statement;
    }
}
