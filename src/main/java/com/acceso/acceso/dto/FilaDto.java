package com.acceso.acceso.dto;

import java.time.LocalDateTime;

public class FilaDto {

    private Long id;
    private LocalDateTime horaToma;
    private String estado;
    private Long ingresoId;
    private String nombre;
    private String modulo;
    private LocalDateTime horaIngreso;
    private Long idModulo;

    
    public String getModulo() {
        return modulo;
    }

    public void setModulo(String modulo) {
        this.modulo = modulo;
    }

    public LocalDateTime getHoraIngreso() {
        return horaIngreso;
    }

    public void setHoraIngreso(LocalDateTime horaIngreso) {
        this.horaIngreso = horaIngreso;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getHoraToma() {
        return horaToma;
    }

    public void setHoraToma(LocalDateTime horaToma) {
        this.horaToma = horaToma;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Long getIngresoId() {
        return ingresoId;
    }

    public void setIngresoId(Long ingresoId) {
        this.ingresoId = ingresoId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getIdModulo() {
        return idModulo;
    }

    public void setIdModulo(Long idMoudlo) {
        this.idModulo = idMoudlo;
    }
}