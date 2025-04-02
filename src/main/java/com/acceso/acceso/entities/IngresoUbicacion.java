package com.acceso.acceso.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "ingreso_ubicacion")
public class IngresoUbicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ingreso_id", nullable = false)
    private Ingreso ingreso;

    @ManyToOne
    @JoinColumn(name = "ubicacion_departamento_id", nullable = false)
    private UbicacionDepartamento ubicacionDepartamento;

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

    public UbicacionDepartamento getUbicacionDepartamento() {
        return ubicacionDepartamento;
    }

    public void setUbicacionDepartamento(UbicacionDepartamento ubicacionDepartamento) {
        this.ubicacionDepartamento = ubicacionDepartamento;
    }

    

}
