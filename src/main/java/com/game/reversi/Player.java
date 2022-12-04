package com.game.reversi;

public abstract class Player {
    private Board.Chip chipColor;
    private int bestScore = 0;

    public Player(Board.Chip chipColor) {
        this.chipColor = chipColor;
    }

    void updateBestScore(final int score) {
        bestScore = Math.max(score, bestScore);
    }

    public int getBestScore() {
        return bestScore;
    }

    public Board.Chip getChipColor() {
        return chipColor;
    }

    // TODO maybe remove
    public static void SwapColors(Player player1, Player player2) {
        Board.Chip temp = player1.chipColor;
        player1.chipColor = player2.chipColor;
        player2.chipColor = temp;
    }
}
