package com.game.reversi;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @DisplayName("Best scores updating")
    @Test
    void getBestScore() {
        var human = new HumanPlayer(Board.Chip.BLACK);
        var bot = new BotPlayer(Board.Chip.WHITE);
        assertAll("Default best scores",
                () -> assertEquals(human.getBestScore(), 0, "default human best score == 1"),
                () -> assertEquals(bot.getBestScore(), 0, "default bot best score == 1")
                );
        human.updateBestScore(-1);
        human.updateBestScore(7);
        human.updateBestScore(-3);
        bot.updateBestScore(5);
        bot.updateBestScore(3);
        assertAll("Updated best scores",
                () -> assertEquals(human.getBestScore(), 7, "human best score == 7 (after -1 7 -3)"),
                () -> assertEquals(bot.getBestScore(), 5, "bot best score == 5 (after 5 3)")
        );
    }

    @DisplayName("Chip color")
    @Test
    void getChipColor() {
        var human = new HumanPlayer(Board.Chip.BLACK);
        var bot = new BotPlayer(Board.Chip.WHITE);
        assertEquals(human.getChipColor(), Board.Chip.BLACK, "black humanPlayer color");
        assertEquals(bot.getChipColor(), Board.Chip.WHITE, "white botPlayer color");
    }
}