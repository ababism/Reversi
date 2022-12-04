package com.game.reversi;

import java.util.Scanner;

public final class Game {

    private Board currentBoard;
    private Board prevBoard;

    //    int bestScore;
    private int counterForTurns = 1;
    private boolean isGameGoing;
    Player[] players = new Player[2];

    public Game() {
        players[0] = new HumanPlayer(Board.Chip.BLACK);
        setSecondPlayer();
        currentBoard = new Board();
        System.out.println("Игра \"Реверси\"!");

    }

    private void menu() {

    }

    public void start() {
        while (isGameGoing) {
            isGameGoing = turn();
        }
        System.out.println("Игра окончена");
        System.out.printf("Победил игрок %s (%d)", players[currentPlayerIndex()].getName(), currentPlayerIndex());

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


    private int currentPlayerIndex() {
        return counterForTurns % 2;
    }

    private void displayStats() {
        System.out.printf("Turn %d of %s (%d) (chip color: %c)", currentBoard.getTurn(), players[currentPlayerIndex()].getName(), currentPlayerIndex(), players[currentPlayerIndex()].getChipColor().chipToChar());
    }
    private boolean turn() {
        counterForTurns += 1;
        currentBoard.DisplayBoard();
        prevBoard = currentBoard;
        players[currentPlayerIndex()].makeTurn(currentBoard);
    }
}
