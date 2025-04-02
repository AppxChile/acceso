package com.acceso.acceso.entities;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Fila {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime horaIngresoFila;

    public LocalDateTime getHoraIngresoFila() {
        return horaIngresoFila;
    }

    public void setHoraIngresoFila(LocalDateTime horaIngresoFila) {
        this.horaIngresoFila = horaIngresoFila;
    }

    public Set<FilaDepartamento> getFilaDepartamentos() {
        return filaDepartamentos;
    }

    public void setFilaDepartamentos(Set<FilaDepartamento> filaDepartamentos) {
        this.filaDepartamentos = filaDepartamentos;
    }

    @ManyToOne
    private Ingreso ingreso;

    @OneToMany(mappedBy = "fila", cascade = CascadeType.ALL)
    private Set<FilaDepartamento> filaDepartamentos = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Ingreso getIngreso() {
        return ingreso;
    }

    public void setIngreso(Ingreso ingreso) {
        this.ingreso = ingreso;
    }

    
}