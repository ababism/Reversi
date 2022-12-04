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

    public Board.Chip getChipColor() {
        return chipColor;
    }
}
