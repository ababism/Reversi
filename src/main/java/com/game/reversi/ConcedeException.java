package com.game.reversi;

/**
 * Исключение при услвии, что игрок сдается
 */
public class ConcedeException extends Exception {
    ConcedeException(String message) {
        super(message);
    }
}
