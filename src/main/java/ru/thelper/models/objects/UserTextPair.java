package ru.thelper.models.objects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import ru.thelper.models.users.User;

@AllArgsConstructor
@Getter
@Builder
public class UserTextPair {

    User user;

    String text;
}
