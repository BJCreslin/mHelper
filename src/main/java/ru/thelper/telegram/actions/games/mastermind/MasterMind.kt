package ru.thelper.telegram.actions.games.mastermind

import ru.thelper.telegram.actions.games.Game

class MasterMind : Game {
    override fun getDescription(): String {
        return """Описание игры"""
    }

    override fun play() {
        var gameStatus: GameStatus  =prepare()
        //todo доделать
    }

    private fun prepare(): GameStatus {
        return GameStatus()
    }
}