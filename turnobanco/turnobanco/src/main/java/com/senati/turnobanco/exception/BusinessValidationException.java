package com.senati.turnobanco.exception;

public class BusinessValidationException extends RuntimeException {
    private final String campo;

    public BusinessValidationException(String campo, String mensaje) {
        super(mensaje);
        this.campo = campo;
    }

    public String getCampo() {
        return campo;
    }
}

