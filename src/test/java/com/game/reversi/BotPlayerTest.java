package com.game.reversi;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BotPlayerTest {

    @DisplayName("Bot name")
    @Test
    void getName() {
        var bot = new BotPlayer(Board.Chip.BLACK);
        assertEquals(bot.getName(), "Bot");
    }
}