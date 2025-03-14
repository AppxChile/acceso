package com.acceso.acceso.dto;

import java.time.LocalDateTime;

public class IngresosByFechasDto {

    private LocalDateTime fechaIngreso;
    private String nombre;
    private String rut;
    private LocalDateTime fechaSalida;

    public LocalDateTime getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDateTime fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombbre) {
        this.nombre = nombbre;
    }

    public LocalDateTime getFechaSalida() {
        return fechaSalida;
    }

    public void setFechaSalida(LocalDateTime fechaSalida) {
        this.fechaSalida = fechaSalida;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

}
