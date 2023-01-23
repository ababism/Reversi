package com.game.reversi;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @DisplayName("Checks if number of starting turn is correct")
    @org.junit.jupiter.api.Test
    void getTurn_at_first_turn() {
        var board = new Board();
        assertEquals(1, board.getTurn(), "First turn has number 1");
    }

    @DisplayName("Checks if number of second turn is correct")
    @org.junit.jupiter.api.Test
    void getTurn_at_next_turns() {
        var board = new Board();
        var bot = new BotPlayer(Board.Chip.BLACK);
        try {
            board.makeTurn(bot);
        } catch (ConcedeException concedeException) {
            assertTrue(true,
                    "Concede Exception, test is not valid because of incorrect makeTurn");
        }
        assertEquals(2, board.getTurn());
    }

    @DisplayName("Check if score count at start is correct B:2 W:2")
    @Test
    void calculateScore_at_start() {
        var board = new Board();
        assertEquals(2, board.calculateScore(Board.Chip.BLACK), "Black score == 2");
        assertEquals(2, board.calculateScore(Board.Chip.WHITE), "White score == 2");
    }

    @DisplayName("Check if score count at second turn is correct B:4 W: 1")
    @org.junit.jupiter.api.Test
    void calculateScore_at_second_turn() {
        var board = new Board();
        var bot = new BotPlayer(Board.Chip.BLACK);
        try {
            board.makeTurn(bot);
        } catch (ConcedeException concedeException) {
            assertTrue(true,
                    "Concede Exception, test is not valid because of incorrect makeTurn");
        }
        assertEquals(4, board.calculateScore(Board.Chip.BLACK), "Amount of black chips == 4");
        assertEquals(1, board.calculateScore(Board.Chip.WHITE), "Amount of white chips == 1");
    }

    @DisplayName("Ability to make 1st 2nd turn")
    @org.junit.jupiter.api.Test
    void playerAbleToMakeTurn() {
        var board = new Board();
        var bot = new BotPlayer(Board.Chip.BLACK);
        assertTrue(board.playerAbleToMakeTurn(), "Can player make first turn");
        try {
            board.makeTurn(bot);
        } catch (ConcedeException concedeException) {
            assertTrue(true,
                    "Concede Exception, test is not valid because of incorrect makeTurn");
        }
        assertTrue(board.playerAbleToMakeTurn(), "Can player make second turn");
    }

    @DisplayName("Is making turn works correct")
    @org.junit.jupiter.api.Test
    void makeTurn() {
        var board = new Board();
        var bot = new BotPlayer(Board.Chip.BLACK);
        boolean flag = board.playerAbleToMakeTurn() && 2 == board.calculateScore(Board.Chip.BLACK) && 2 == board.calculateScore(Board.Chip.WHITE);
        try {
            board.makeTurn(bot);
        } catch (ConcedeException concedeException) {
            assertTrue(true,
                    "Concede Exception, test is not valid because of incorrect makeTurn");
        }
        assertTrue(flag, "test wont be valid in spite of wrong starting conditions");
        assertTrue(board.playerAbleToMakeTurn(), "Player make second turn");
        assertTrue( 4 == board.calculateScore(Board.Chip.BLACK) && 1 == board.calculateScore(Board.Chip.WHITE), "Is scores updating in the right way");
    }
}