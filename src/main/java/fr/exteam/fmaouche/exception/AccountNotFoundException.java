package fr.exteam.fmaouche.exception;

public class AccountNotFoundException extends RuntimeException {

    private String message;

    public AccountNotFoundException(String message) {
        this.message = message;
    }
}
