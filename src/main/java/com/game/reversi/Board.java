package com.game.reversi;

import java.util.Scanner;

/**
 * Класс доски, реализует все операции с ней, также содержит enum Chip
 */
public class Board {
    /**
     * Заведем константы (почти все будут нужны нам только внутри класса)
     * Это хорошая практика, чтобы внутри кода не было "волшебных" цифр
     */
    final static public int BOARD_SIZE = 8;
    /**
     * Веса для ИИ, соотношение весов сохранено согласно примеру
     */
    // Веса клеток которые мы зажимаем
    final static private int SE = 20;
    final static private int SI = 10;
    // Веса клетки на которую мы ставим фишку
    final static private int SSC = 8;
    final static private int SSE = 4;
    final static private int SSI = 0;

    private Chip playerColor;
    private Chip opponentsColour;
    /**
     * Физический ход
     */
    private int turn;

    private final Chip[][] desk = new Chip[BOARD_SIZE][BOARD_SIZE];

    public int getTurn() {
        return turn;
    }

    /**
     * Консркутор согласно правилам игры
     */
    public Board() {
        turn = 1;
        playerColor = Chip.BLACK;
        opponentsColour = Chip.WHITE;
        desk[3][4] = Chip.BLACK;
        desk[4][3] = Chip.BLACK;
        desk[3][3] = Chip.WHITE;
        desk[4][4] = Chip.WHITE;
    }

    /**
     * Констркуктор копирования
     *
     * @param other доска, которую мы хотим скопировать
     */
    public Board(Board other) {
        for (int i = 0; i < BOARD_SIZE; ++i) {
            System.arraycopy(other.desk[i], 0, desk[i], 0, BOARD_SIZE);
        }
        turn = other.turn;
        playerColor = other.playerColor;
        opponentsColour = other.opponentsColour;
    }

    /**
     * Наша фишка, почти везде удобно называть ее цветом, так как фишка принимает только значения ее цвета
     * Chip == null обозначает, что фишки нет, что логично
     */
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

    /**
     * Считает количество фишек на поле
     *
     * @param colour цвет фишек
     * @return колиство фишек данного цвета
     */
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

    /**
     * Переводит фишку в символ
     *
     * @param chip фишка
     * @param x    координата на доске
     * @param y    координата на доске
     * @return симвон, обозначающий фишку или ее отсутсвие
     */
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


    /**
     * Проверяет координаты на корректность
     *
     * @param x координата на доске
     * @param y координата на доске
     * @return Кореектны ли координаты
     */
    private boolean checkCoordinates(int x, int y) {
        return x >= 0 && y >= 0 && x < BOARD_SIZE && y < BOARD_SIZE;
    }

    /**
     * Считает вес замещения зажатой фишки для ИИ
     *
     * @param x координата на фишки
     * @param y координата на фишки
     * @return вес фишки
     */
    private int countWeightOfInsideChip(final int x, final int y) {
        if (x == 0 || y == 0 || x == BOARD_SIZE - 1 || y == BOARD_SIZE - 1) {
            return SE;
        } else {
            return SI;
        }
    }

    /**
     * Считает вес поставленной фишки для ИИ
     *
     * @param x координата на фишки
     * @param y координата на фишки
     * @return вес фишки
     */
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

    /**
     * Считает вес фишек, которые можно заменить по направлению заданным нормальным единичным вектором
     * от фишки которую поставят
     *
     * @param x  координата на фишки которую поставят
     * @param y  координата на фишки которую поставят
     * @param vX координата нормального единичного вектора
     * @param vY координата нормального единичного вектора
     * @return Вес замены, 0 если нельзя совершить ход (не будет ни одного переворота)
     */
    private int calculateValueOfDirection(int x, int y, int vX, int vY) {
        if (!checkCoordinates(x, y) || (vX == 0 && vY == 0) || desk[x][y] != null) {
            return 0;
        }
//        int weightCount = countWeightOfChipPlaced(x, y);
        int weightCount = 0;

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

    /**
     * Переворачивает все потенциально зажатые фишки по напрвелению заданным нормальным единичным вектором
     *
     * @param x  координата на фишки которую поставят
     * @param y  координата на фишки которую поставят
     * @param vX координата нормального единичного вектора
     * @param vY координата нормального единичного вектора
     */
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

    /**
     * Ставит фишку на опредленное место согласно правилам
     *
     * @param x координата фишки
     * @param y координата фишки
     */
    private void placeChipAt(int x, int y) {
        for (var vX = -1; vX < 2; ++vX) {
            for (var vY = -1; vY < 2; ++vY) {
                flipChipsInDirection(x, y, vX, vY);
            }
        }
        desk[x][y] = playerColor;
    }

    /**
     * Считает вес хода, где ставят фишку (т.е. сумма весов заменяемых)
     *
     * @param x координата фишки
     * @param y координата фишки
     * @return вес фишки
     */
    private int chipToPlaceValue(int x, int y) {
        int count = 0;
        for (var vX = -1; vX < 2; ++vX) {
            for (var vY = -1; vY < 2; ++vY) {
                count += calculateValueOfDirection(x, y, vX, vY);
            }
        }
        return count;
    }

    /**
     * Можно ли поставить фишку
     *
     * @param x координата фишки
     * @param y координата фишки
     * @return Можно ли поставить фишку
     */
    private boolean isChipPlaceableAt(int x, int y) {
        if (x >= BOARD_SIZE || x < 0 || y >= BOARD_SIZE || y < 0) {
            return false;
        }
        return chipToPlaceValue(x, y) > 0;
    }

    /**
     * Отрисовывает доску
     */
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

    /**
     * Может ли хоядщий игрок сделать ход
     *
     * @return Может ли хоядщий игрок сделать ход
     */
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

    /**
     * Поменять цвет на цвет ирогка если надо
     *
     * @param player цвет игрока, чей цвеь нужно выбьрать
     */
    private void changeColorToPlayer(Player player) {
        if (player.getChipColor() != playerColor) {
            opponentsColour = playerColor;
            playerColor = player.getChipColor();
        }
    }

    /**
     * ИИ сравнивает веса всевозможных ходов ставит фишку в наилучшиую позицию
     */
    private void placeBestBotTurn() {
        int bestX = 0;
        int bestY = 0;
        int maxPlacementValue = 0;
        for (var x = 0; x < BOARD_SIZE - 1; ++x) {
            for (var y = 0; y < BOARD_SIZE - 1; ++y) {
                if (chipToPlaceValue(x, y) + countWeightOfChipPlaced(x, y) > maxPlacementValue) {
                    maxPlacementValue = chipToPlaceValue(x, y) + countWeightOfChipPlaced(x, y);
                    bestX = x;
                    bestY = y;
                }
            }
        }
        placeChipAt(bestX, bestY);
        System.out.printf("Бот ходит %d %d\n", bestX + 1, bestY + 1);

    }

    /**
     * Условие заверщения игры
     *
     * @return завершена ли игра
     */
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

    /**
     * Меняет местами цвета игроков
     */
    private void swapPlayerColors() {
        Chip color = playerColor;
        playerColor = opponentsColour;
        opponentsColour = color;
    }
}
