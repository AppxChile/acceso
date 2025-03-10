package com.acceso.acceso.dto;

import java.time.LocalDateTime;

public class IngresosByFechasDto {

    private LocalDateTime fechaIngreso;
    private String nombre;
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

    



}
