package ru.mhelper.models.users;

public enum TelegramStateType {

    NO_STATEMENT("NO STATEMENT");

    public final String statement;

    public String getStatement() {
        return statement;
    }

    TelegramStateType(String statement) {
        this.statement = statement;
    }
}
