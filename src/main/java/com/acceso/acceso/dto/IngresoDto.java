package com.acceso.acceso.dto;

import java.time.LocalDateTime;
import java.util.List;

public class IngresoDto {

    private Long id;
    private LocalDateTime horaIngreso;
    private PersonaDto persona;
    private List<Long> departamentos;
    private String estadoFila;

    private List<CitaResponse> cita;

    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getHoraIngreso() {
        return horaIngreso;
    }

    public void setHoraIngreso(LocalDateTime horaIngreso) {
        this.horaIngreso = horaIngreso;
    }

    public PersonaDto getPersona() {
        return persona;
    }

    public void setPersona(PersonaDto persona) {
        this.persona = persona;
    }

    public List<Long> getDepartamentos() {
        return departamentos;
    }

    public void setDepartamentos(List<Long> departamentos) {
        this.departamentos = departamentos;
    }

    public String getEstadoFila() {
        return estadoFila;
    }

    public void setEstadoFila(String estadoFila) {
        this.estadoFila = estadoFila;
    }

    public List<CitaResponse> getCita() {
        return cita;
    }

    public void setCita(List<CitaResponse> cita) {
        this.cita = cita;
    }



}
