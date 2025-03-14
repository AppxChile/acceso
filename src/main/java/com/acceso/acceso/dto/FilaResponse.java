package com.acceso.acceso.dto;

public class FilaResponse {
    private Long id;
    private String asignadoA;
    private String estado;

    public FilaResponse(Long id, String asignadoA, String estado) {
        this.id = id;
        this.asignadoA = asignadoA;
        this.estado = estado;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAsignadoA() {
        return asignadoA;
    }

    public void setAsignadoA(String asignadoA) {
        this.asignadoA = asignadoA;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

}