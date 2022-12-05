package com.game.reversi;

/**
 * Класс игрока, нужен, чтобы удобно реализовать игру, а также инкапсулировать переменные
 */
public abstract class Player {
    private final Board.Chip chipColor;
    private int bestScore = 0;

    /**
     * Абстрактный метод, чтобы получать точное имя, кем бы игрок ни был
     *
     * @return имя игрока
     */
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
