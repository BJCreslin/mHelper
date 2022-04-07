package ru.mhelper.models.users;

public enum TelegramStateType {

    NO_STATEMENT("NO STATEMENT"),

    NEW_TELEGRAM_USER("NEW TELEGRAM USER"),

    GETTING_CODE("GETTING_CODE");

    public final String statement;

    public String getStatement() {
        return statement;
    }

    TelegramStateType(String statement) {
        this.statement = statement;
    }
}
