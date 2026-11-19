package com.senati.turnobanco.exception;

public class ResourceNotFoundException extends RuntimeException {
    private final String campo;

    public ResourceNotFoundException(String campo, String mensaje) {
        super(mensaje);
        this.campo = campo;
    }

    public String getCampo() {
        return campo;
    }
}
