package ru.thelper.models.objects;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.function.Function;

@AllArgsConstructor
@Getter
public final class TelegramNameFunctionPair {

    private final String name;

    private final Function<Long, String> action;
}
