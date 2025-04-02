package com.acceso.acceso.dto;

import java.util.Set;

public class IngresoRequest {

    private Integer rut;

    private String serie;
    private Long idUbicacion;
    private Set<Long> idDepartamentos;

    public Integer getRut() {
        return rut;
    }

    public void setRut(Integer rut) {
        this.rut = rut;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public Set<Long> getIdDepartamentos() {
        return idDepartamentos;
    }

    public void setIdDepartamentos(Set<Long> idDepartamentos) {
        this.idDepartamentos = idDepartamentos;
    }

    public Long getIdUbicacion() {
        return idUbicacion;
    }

    public void setIdUbicacion(Long idUbicacion) {
        this.idUbicacion = idUbicacion;
    }

}
