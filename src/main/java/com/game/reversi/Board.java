package com.game.reversi;


import java.util.Arrays;

public class Board {
    final static public int BOARD_SIZE = 8;
    final static private String BOARD_FORMAT = """
            -------------------------
            |%c|%c|%c|%c|%c|%c|%c|%c|
            -------------------------
            |%c|%c|%c|%c|%c|%c|%c|%c|
            -------------------------
            |%c|%c|%c|%c|%c|%c|%c|%c|
            -------------------------
            |%c|%c|%c|%c|%c|%c|%c|%c|
            -------------------------
            |%c|%c|%c|%c|%c|%c|%c|%c|
            -------------------------
            |%c|%c|%c|%c|%c|%c|%c|%c|
            -------------------------
            |%c|%c|%c|%c|%c|%c|%c|%c|
            -------------------------
            |%c|%c|%c|%c|%c|%c|%c|%c|
            """;
    private final Chip[][] desk = new Chip[BOARD_SIZE][BOARD_SIZE];

    public Board() {
        desk[3][4] = Chip.BLACK;
        desk[4][3] = Chip.BLACK;
        desk[3][3] = Chip.WHITE;
        desk[4][4] = Chip.WHITE;
    }

    enum Chip {
        BLACK, WHITE;

        static char chipToChar(Chip chip) {
            if (chip == WHITE) {
                return '◍';
            } else if (chip == BLACK) {
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

    private static char chipToChar(Chip chip, int x, int y) {
        if (chip == Chip.WHITE) {
            return '◍';
        } else if (chip == Chip.BLACK) {
            return '◯';
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

    //TODO
    boolean isChipPlaceableAt(Chip color, int x, int y) {
        if (x >= BOARD_SIZE || x < 0 || y >= BOARD_SIZE || y < 0) {
            return false;
        }
        //TODO
        return false;
    }

    public void DisplayBoard() {
//        System.out.println("_________________");
        System.out.println("_|1|2|3|4|5|6|7|8|");
        for (int i = 0; i < 8; i++) {
            System.out.print(i+1);
            System.out.printf("|%c|%c|%c|%c|%c|%c|%c|%c|\n", Arrays.stream(desk[i]).map(Chip::chipToChar).toArray());
        }
//        for (var chipArr : desk) {
//            System.out.printf("|%c|%c|%c|%c|%c|%c|%c|%c|\n", Arrays.stream(chipArr).map(Chip::chipToChar).toArray());
//        }
        //  System.out.printf(BOARD_FORMAT, Arrays.stream(desk).map(i -> Arrays.stream(i).map(Chip::chipToChar)));
    }
}
