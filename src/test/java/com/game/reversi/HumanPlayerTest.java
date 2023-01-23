package com.game.reversi;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HumanPlayerTest {

    @DisplayName("Human player name")
    @Test
    void getName() {
        var human = new HumanPlayer(Board.Chip.BLACK);
        assertEquals(human.getName(), "Player");
    }
}