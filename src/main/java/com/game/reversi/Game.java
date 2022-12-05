package com.game.reversi;

import java.util.Scanner;
import java.util.Stack;

public final class Game {

    private Board currentBoard;
    private Stack<Board> boardStack;
    private int counterForTurns = 0;
    Player[] players = new Player[2];

    public Game() {
        players[0] = new HumanPlayer(Board.Chip.BLACK);
        setSecondPlayer();
        currentBoard = new Board();
        boardStack = new Stack<>();
        boardStack.push(new Board(currentBoard));
        System.out.println("Игра \"Реверси\"!");
    }

    private void displayBestScores() {
        System.out.printf("Best scores: %s_0 -- %d | %s_1 -- %d\n", players[0].getName(), players[0].getBestScore(),
                players[1].getName(), players[1].getBestScore());
    }

    public void menu() {
        boolean quitFlag = false;
        while (!quitFlag) {
            System.out.println("МЕНЮ");
            displayBestScores();
            System.out.println("""
                    Введите одну из следующих цифр:
                    1. Начать игру
                    2. Сменить противника (его лучший результат сброситься)
                    0. Выйти
                    """);
            var scanner = new Scanner(System.in);
            switch (scanner.nextInt()) {
                case 1:
                    start();
                    break;
                case 2:
                    setSecondPlayer();
                    break;
                case 0:
                    quitFlag = true;
                default:
                    break;
            }
        }
    }

    private void displayScores() {
        System.out.printf("Игрок %s_%d: %d\n", currentPlayer().getName(), currentPlayerIndex(), currentBoard.calculateScore(currentPlayer().getChipColor()));
        System.out.printf("Игрок %s_%d: %d\n", opponent().getName(), opponentIndex(), currentBoard.calculateScore(opponent().getChipColor()));
    }

    private void start() {
        // TODO reset board
        counterForTurns = 0;
        currentBoard = new Board();
        boardStack = new Stack<>();
        boardStack.push(new Board(currentBoard));
        System.out.println("Игра началась");
        try {

            while (!currentBoard.finishCondition()) {
                System.out.println("--------------------------" + counterForTurns);

                displayScores();
                displayCurrPlayerTurnStats();
                if (!currentBoard.makeTurn(currentPlayer())) {
                    if (counterForTurns >= 2) {
                        boardStack.pop();
                        boardStack.pop();
                        currentBoard = new Board(boardStack.peek());
                        counterForTurns -= 2;
                    } else {
                        System.out.println("Нельзя отменить ход!");
                    }
                } else {
                    ++counterForTurns;
                    boardStack.push(new Board(currentBoard));
                }
            }
        } catch (ConcedeException ex) {
            System.out.println(ex.getMessage());
        }
        System.out.println("Игра окончена");
        currentBoard.displayBoard();
        displayScores();
        currentPlayer().updateBestScore(currentBoard.calculateScore(currentPlayer().getChipColor()));
        opponent().updateBestScore(currentBoard.calculateScore(opponent().getChipColor()));
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

    private Player currentPlayer() {
        return players[currentPlayerIndex()];
    }

    private int opponentIndex() {
        return (counterForTurns + 1) % 2;
    }

    private Player opponent() {
        return players[opponentIndex()];
    }

    private void displayCurrPlayerTurnStats() {
        System.out.printf("Turn %d of %s (%d) (chip color: %c)\n", currentBoard.getTurn(), currentPlayer().getName(), currentPlayerIndex(), currentPlayer().getChipColor().chipToChar());
    }

}
