package com.game.reversi;

import java.util.Scanner;

public class Board {
    final static public int BOARD_SIZE = 8;
    // Веса клеток которые мы зажимаем
    final static private int SE = 20;
    final static private int SI = 10;
    // Веса клетки на которую мы ставим фишку
    final static private int SSC = 8;
    final static private int SSE = 4;
    final static private int SSI = 0;

    private Chip playerColor;
    private Chip opponentsColour;
    private int turn;

    private final Chip[][] desk = new Chip[BOARD_SIZE][BOARD_SIZE];

    public int getTurn() {
        return turn;
    }

    public Board() {
        turn = 1;
        playerColor = Chip.BLACK;
        opponentsColour = Chip.WHITE;
        desk[3][4] = Chip.BLACK;
        desk[4][3] = Chip.BLACK;
        desk[3][3] = Chip.WHITE;
        desk[4][4] = Chip.WHITE;
    }

    public Board(Board other) {
        for (int i = 0; i < BOARD_SIZE; ++i) {
            System.arraycopy(other.desk[i], 0, desk[i], 0, BOARD_SIZE);
        }
        turn = other.turn;
        playerColor = other.playerColor;
        opponentsColour = other.opponentsColour;
    }

    enum Chip {
        BLACK, WHITE;

        public char chipToChar() {
            if (this == WHITE) {
                return '◍';
            } else if (this == BLACK) {
                return '◯';
            } else {
                return '_';
            }
        }
    }

    public int calculateScore(Chip colour) {
        int count = 0;
        for (var chipArr : desk) {
            for (var chip : chipArr) {
                if (chip == colour) {
                    ++count;
                }
            }
        }
        return count;
    }

    private char chipToChar(Chip chip, int x, int y) {
        if (chip == Chip.WHITE) {
            return '◍';
        } else if (chip == Chip.BLACK) {
            return '◯';
        } else if (chip == null && isChipPlaceableAt(x, y)) {
            return '*';
        } else {
            return '_';
        }
    }


    // Good code
    private boolean checkCoordinates(int x, int y) {
        return x >= 0 && y >= 0 && x < BOARD_SIZE && y < BOARD_SIZE;
    }

    private int countWeightOfInsideChip(final int x, final int y) {
        if (x == 0 || y == 0 || x == BOARD_SIZE - 1 || y == BOARD_SIZE - 1) {
            return SE;
        } else {
            return SI;
        }
    }

    private int countWeightOfChipPlaced(final int x, final int y) {
        if (!checkCoordinates(x, y)) {
            return 0;
        }

        if ((x == 0 && y == 0) || (x == BOARD_SIZE - 1 && y == BOARD_SIZE - 1)
                || (x == 0 && y == BOARD_SIZE - 1) || (x == BOARD_SIZE - 1 && y == 0)) {
            return SSC;
        } else if (x == 0 || y == 0 || x == BOARD_SIZE - 1 || y == BOARD_SIZE - 1) {
            return SSE;
        } else {
            return SSI;
        }
    }

    private int CalculateValueOfDirection(int x, int y, int vX, int vY) {
        if (!checkCoordinates(x, y) || (vX == 0 && vY == 0) || desk[x][y] != null) {
            return 0;
        }
        int weightCount = countWeightOfChipPlaced(x, y);
        int chipCount = 0;
        x += vX;
        y += vY;
        while (x > 0 && y > 0 && x < BOARD_SIZE - 2 && y < BOARD_SIZE - 2 && desk[x][y] == opponentsColour) {
            weightCount += countWeightOfInsideChip(x, y);
            x += vX;
            y += vY;
            ++chipCount;
        }
        if (x >= 0 && y >= 0 && x < BOARD_SIZE - 1 && y < BOARD_SIZE - 1 && desk[x][y] == playerColor && chipCount > 0) {
            return weightCount;
        } else {
            return 0;
        }
    }

    private void flipChipsInDirection(int x, int y, int vX, int vY) {
        if (!checkCoordinates(x, y) || (vX == 0 && vY == 0) || desk[x][y] != null) {
            return;
        }
        int chipCount = 0;
        x += vX;
        y += vY;
        var copyOfX = x;
        var copyOfY = y;
        while (x > 0 && y > 0 && x < BOARD_SIZE - 2 && y < BOARD_SIZE - 2 && desk[x][y] == opponentsColour) {
            x += vX;
            y += vY;
            ++chipCount;
        }
        if (x >= 0 && y >= 0 && x < BOARD_SIZE - 1 && y < BOARD_SIZE - 1 && desk[x][y] == playerColor && chipCount > 0) {
            for (var chipIt = 0; chipIt < chipCount; ++chipIt) {
                desk[copyOfX][copyOfY] = desk[copyOfX][copyOfY] == Chip.WHITE ? Chip.BLACK : Chip.WHITE;
                copyOfX += vX;
                copyOfY += vY;
            }
        }
    }

    private void placeChipAt(int x, int y) {
        for (var vX = -1; vX < 2; ++vX) {
            for (var vY = -1; vY < 2; ++vY) {
                flipChipsInDirection(x, y, vX, vY);
            }
        }
        desk[x][y] = playerColor;
    }


    private int chipToPlaceValue(int x, int y) {
        int count = 0;
        for (var vX = -1; vX < 2; ++vX) {
            for (var vY = -1; vY < 2; ++vY) {
                count += CalculateValueOfDirection(x, y, vX, vY);
            }
        }
        return count;
    }

    private boolean isChipPlaceableAt(int x, int y) {
        if (x >= BOARD_SIZE || x < 0 || y >= BOARD_SIZE || y < 0) {
            return false;
        }
        return chipToPlaceValue(x, y) > 0;
    }

    public void displayBoard() {
        System.out.println("_|1|2|3|4|5|6|7|8|");
        for (int i = 0; i < BOARD_SIZE; ++i) {
            System.out.print(i + 1);
            for (int j = 0; j < BOARD_SIZE; ++j) {
                System.out.printf("|%c", chipToChar(desk[i][j], i, j));
            }
            System.out.print("|\n");
        }
    }


    public boolean playerAbleToMakeTurn() {
        for (var x = 0; x < BOARD_SIZE - 1; ++x) {
            for (var y = 0; y < BOARD_SIZE - 1; ++y) {
                if (isChipPlaceableAt(x, y)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Меняет игрока на доске на текущего, обновляет счетких ходов
     *
     * @param player игрок совершающий ход
     * @return Не был ли ход отменен
     * @throws ConcedeException Исключение, когда кто-то сдался
     */
    public boolean makeTurn(Player player) throws ConcedeException {
        changeColorToPlayer(player);
        displayBoard();
        if (!playerAbleToMakeTurn()) {
            return true;
        }

        if (player instanceof BotPlayer) {
            placeBestBotTurn();
        } else {
            int x, y;
            Scanner scanner = new Scanner(System.in);
            System.out.println("Введите координаты вашего хода (или -1 сдаться; или -2 отменить ход)");
            x = scanner.nextInt() - 1;
            if (x == -2) {
                throw new ConcedeException("Player " + player.getName() + '(' + player.getChipColor().chipToChar() + ") conceded");
            }
            if (x == -3) {
                System.out.print("Отмена хода\n");
                return false;
            }
            y = scanner.nextInt() - 1;

            while (!isChipPlaceableAt(x, y)) {
                System.out.print("Такой ход нельзя сделать, пожалуйста введите кооринаты одной из клеток обозаченных *\n");
                System.out.println("Введите координаты вашего хода (или -1 сдаться; или -2 отменить ход)");
                x = scanner.nextInt() - 1;
                if (x == -2) {
                    throw new ConcedeException("Player " + player.getName() + '(' + player.getChipColor().chipToChar() + ") conceded");
                }
                if (x == -3) {
                    System.out.print("Отмена хода:\n");
                    return false;
                }
                y = scanner.nextInt() - 1;
            }
            placeChipAt(x, y);
        }
        ++turn;
        return true;
    }

    private void changeColorToPlayer(Player player) {
        if (player.getChipColor() != playerColor) {
            opponentsColour = playerColor;
            playerColor = player.getChipColor();
        }
    }

    private void placeBestBotTurn() {
        int bestX = 0;
        int bestY = 0;
        int maxPlacementValue = 0;
        for (var x = 0; x < BOARD_SIZE - 1; ++x) {
            for (var y = 0; y < BOARD_SIZE - 1; ++y) {
                if (chipToPlaceValue(x, y) > maxPlacementValue) {
                    maxPlacementValue = chipToPlaceValue(x, y);
                    bestX = x;
                    bestY = y;
                }
            }
        }
//        System.out.printf("Бот value %d\n", chipToPlaceValue(bestX, bestY));
        placeChipAt(bestX, bestY);
        System.out.printf("Бот ходит %d %d\n", bestX + 1, bestY + 1);

    }

    public boolean finishCondition() {
        if (!playerAbleToMakeTurn()) {
            swapPlayerColors();
            if (!playerAbleToMakeTurn()) {
                swapPlayerColors();
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private void swapPlayerColors() {
        Chip color = playerColor;
        playerColor = opponentsColour;
        opponentsColour = color;
    }
}
