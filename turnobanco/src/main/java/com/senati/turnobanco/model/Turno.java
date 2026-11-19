package com.senati.turnobanco.model;

public class Turno {
    private Long id;
    private String codigo;
    private String cliente;
    private String tipoOperacion;
    private String estado;

    public Turno() {
    }

    public Turno(Long id, String codigo, String cliente, String tipoOperacion, String estado) {
        this.id = id;
        this.codigo = codigo;
        this.cliente = cliente;
        this.tipoOperacion = tipoOperacion;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getTipoOperacion() {
        return tipoOperacion;
    }

    public void setTipoOperacion(String tipoOperacion) {
        this.tipoOperacion = tipoOperacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
