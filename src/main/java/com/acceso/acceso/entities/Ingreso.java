package com.acceso.acceso.entities;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Ingreso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime horaIngreso;

    private Integer rut;

    private String serie;

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

    public LocalDateTime getHoraSalida(){
        return this.salida.getHoraSalida();
    }

}
