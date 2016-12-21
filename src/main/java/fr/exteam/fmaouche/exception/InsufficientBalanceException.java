package fr.exteam.fmaouche.exception;


public class InsufficientBalanceException extends RuntimeException {

    private String message;

    public InsufficientBalanceException(String message) {
        this.message = message;
    }
}
