package ru.thelper.services.geting_code;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserIdTimed {
    private final Long userId;
    private final LocalDateTime timeCreated;

    public UserIdTimed(Long userId) {
        this.userId = userId;
        this.timeCreated = LocalDateTime.now();
    }
}
