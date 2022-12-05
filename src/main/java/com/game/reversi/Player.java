package com.game.reversi;

public abstract class Player {
    private final Board.Chip chipColor;
    private int bestScore = 0;

    abstract String getName();

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

}
