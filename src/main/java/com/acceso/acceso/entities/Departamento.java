package com.acceso.acceso.entities;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class Departamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ubicacion_id", nullable = false)
    private UbicacionDepartamento ubicacion;

    @OneToMany(mappedBy = "departamento", cascade = CascadeType.ALL)
    private Set<FilaDepartamento> filaDepartamentos = new HashSet<>();




    @OneToMany(mappedBy = "departamento")
    private List<IngresoDepartamento> ingresoDepartamentos;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<IngresoDepartamento> getIngresoDepartamentos() {
        return ingresoDepartamentos;
    }

    public void setIngresoDepartamentos(List<IngresoDepartamento> ingresoDepartamentos) {
        this.ingresoDepartamentos = ingresoDepartamentos;
    }

}