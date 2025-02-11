package com.acceso.acceso.entities;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Ingreso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime horaIngreso;

    @ManyToOne
    private Persona persona;

    @OneToMany(mappedBy = "ingreso")
    private List<IngresoDepartamento> ingresoDepartamentos;

    @OneToMany(mappedBy = "ingreso")
    private List<Fila> filas;

    @OneToOne(mappedBy = "ingreso")
    private Salida salida;

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

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public List<IngresoDepartamento> getIngresoDepartamentos() {
        return ingresoDepartamentos;
    }

    public void setIngresoDepartamentos(List<IngresoDepartamento> ingresoDepartamentos) {
        this.ingresoDepartamentos = ingresoDepartamentos;
    }

    public List<Fila> getFilas() {
        return filas;
    }

    public void setFilas(List<Fila> filas) {
        this.filas = filas;
    }

    public Salida getSalida() {
        return salida;
    }

    public void setSalida(Salida salida) {
        this.salida = salida;
    }

    
}
