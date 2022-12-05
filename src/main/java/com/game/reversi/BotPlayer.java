package com.game.reversi;

public class BotPlayer extends Player {
    private static final String BOT_NAME = "Bot";

    @Override
    String getName() {
        return BOT_NAME;
    }

    public BotPlayer(Board.Chip chipColor) {
        super(chipColor);
    }

}
