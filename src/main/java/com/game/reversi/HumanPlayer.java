package com.game.reversi;

public class HumanPlayer extends Player {
    private static final String HUMAN_NAME = "Player";

    @Override
    String getName() {
        return HUMAN_NAME;
    }

    //    private boolean isHardDifficulty = false;
    public HumanPlayer(Board.Chip chipColor) {
        super(chipColor);
    }

}
