package ru.mhelper.services.telegram.actions.admin_menu;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

public interface AdminTgMenu {

    SendMessage sendInlineKeyBoardMessage(long chatId);

    String getMenuMessageText(Long chatId, String text);

    String getInfo();
}
