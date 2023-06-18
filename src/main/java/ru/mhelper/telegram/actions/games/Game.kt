package ru.mhelper.telegram.actions.games

/**
 * Интерфейс игр
 */
interface Game {
    /**
     * Описание игры
     */
    fun getDescription(): String

    /**
     * Запуск игры
     */
    fun play()

}