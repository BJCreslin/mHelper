package ru.zhelper.zhelper.models.users;

public enum ERole {
    ROLE_USER("USER"),
    ROLE_ADMIN("ADMIN"),
    ROLE_TELEGRAM("TELEGRAM"),
    CHROME_EXTENSION("CHROME_EXTENSION"),
    ROLE_EMAIL("EMAIL");

    private final String name;

    public String getName() {
        return name;
    }

    ERole(String name) {
        this.name = name;
    }
}
