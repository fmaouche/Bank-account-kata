package fr.exteam.fmaouche.exception;


public class NegativeAmountException extends RuntimeException {

    private String message;

    public NegativeAmountException(String message) {
        this.message = message;
    }
}
