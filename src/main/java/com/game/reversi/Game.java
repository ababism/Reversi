package com.game.reversi;

import java.util.Scanner;

public final class Game {

    //    int bestScore;
    int counterForTurns = 0;
    Player[] players = new Player[2];

    public Game() {
    players[0] = new HumanPlayer(Board.Chip.BLACK);
    setSecondPlayer();
    }

    static public void Start() {
    }

    static public void config() {

    }

    private void setSecondPlayer() {
        System.out.print("""
                   Choose who you will play against
                   input 0 to play vs bot
                   input 1 to play vs another player via console:
                """);
        var scanner = new Scanner(System.in);
        if (scanner.nextInt() == 0) {
            players[1] = new BotPlayer(Board.Chip.WHITE);
        } else {
            players[1] = new HumanPlayer(Board.Chip.WHITE);
        }
    }

    private void turn() {
    }
}
