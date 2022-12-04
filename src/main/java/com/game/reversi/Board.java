package com.game.reversi;

import java.util.Arrays;

public class Board {
    final static public int BOARD_SIZE = 8;
    // Веса клеток которые мы зажимаем
    final static public int SE = 20;
    final static public int SI = 10;
    // Веса клетки на которую мы ставим фишку
    final static public int SSC = 8;
    final static public int SSE = 4;
    final static public int SSI = 0;

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

    int calculateScore(Chip colour) {
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

    void placeChipAt(Chip color, int x, int y) throws IllegalArgumentException {
        if (x >= BOARD_SIZE || x < 0 || y >= BOARD_SIZE || y < 0) {
            throw new IllegalArgumentException("Wrong chip coordinates");
        }
        desk[x][y] = color;

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
        //        if (!coordinateInRange(x) || !coordinateInRange(y)) {
        //            return 0;
        //        }
        // TODO проверка на норм вектор
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

    //TODO
    private boolean isChipPlaceableAt(int x, int y) {
        if (x >= BOARD_SIZE || x < 0 || y >= BOARD_SIZE || y < 0) {
            return false;
        }
        int count = 0;
        for (var vX = -1; vX < 2; ++vX) {
            for (var vY = -1; vY < 2; ++vY) {
                count += CalculateValueOfDirection(x, y, vX, vY);
            }
        }
        //TODO
        return count > 0;
    }

    public void DisplayBoard() {
        System.out.println("_|1|2|3|4|5|6|7|8|");
        for (int i = 0; i < BOARD_SIZE; ++i) {
            System.out.print(i + 1);
            for (int j = 0; j < BOARD_SIZE; ++j) {
                System.out.printf("|%c", chipToChar(desk[i][j], i, j));
            }
            System.out.print("|\n");
        }
    }

    public void test() {
        System.out.println(isChipPlaceableAt(2, 2));
    }
}
