package com.security.security.exception;

public class PasswordInvalidException extends RuntimeException {
    public PasswordInvalidException() {
        super("Senha inválida");
    }
}
