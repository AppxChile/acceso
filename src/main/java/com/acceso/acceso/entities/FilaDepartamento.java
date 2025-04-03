package com.acceso.acceso.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class FilaDepartamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "fila_id", nullable = false)
    private Fila fila;

    @ManyToOne
    @JoinColumn(name = "departamento_id", nullable = false)
    private Departamento departamento;

    @ManyToOne
    @JoinColumn(name = "estado_id", nullable = false)
    private Estado estado; // Para saber si sigue en espera, atendido, etc.

    @ManyToOne
    @JoinColumn(name = "modulo_id")
    private Modulo modulo; // Relación con el modulo

    private String asignadoA;

    private LocalDateTime horaToma;

    private LocalDateTime horaFinalizacion;

    public LocalDateTime getHoraToma() {
        return horaToma;
    }

    public void setHoraToma(LocalDateTime horaToma) {
        this.horaToma = horaToma;
    }

    public LocalDateTime getHoraFinalizacion() {
        return horaFinalizacion;
    }

    public void setHoraFinalizacion(LocalDateTime horaFinalizacion) {
        this.horaFinalizacion = horaFinalizacion;
    }

    public Modulo getModulo() {
        return modulo;
    }

    public void setModulo(Modulo modulo) {
        this.modulo = modulo;
    }

    public String getAsignadoA() {
        return asignadoA;
    }

    public void setAsignadoA(String asignadoA) {
        this.asignadoA = asignadoA;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Fila getFila() {
        return fila;
    }

    public void setFila(Fila fila) {
        this.fila = fila;
    }

    public Departamento getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Departamento departamento) {
        this.departamento = departamento;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Long getIngresoId(){
        return this.fila.getIngreso().getId();
    }

    public String getEstadoNombre(){
        return this.estado.getNombre();
    }

    public String getModuloNombre(){
        if (this.modulo != null) {
            return this.modulo.getNombre();
        } else {
            return null; // O un valor predeterminado, o lanzar una excepción
        }

    }

    public LocalDateTime getHoraIngresoFila(){
        return this.fila.getHoraIngresoFila();
    }

    public Long getModuloId(){
        if (this.modulo != null) {
            return this.modulo.getId();
        } else {
            return null; // O un valor predeterminado, o lanzar una excepción
        }
    }

    public Integer getRutIngreso(){
        return this.fila.getIngreso().getRut();
    }

    public Long getFilaId(){
        return this.fila.getId();
    }

}
