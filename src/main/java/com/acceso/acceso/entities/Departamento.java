package com.acceso.acceso.entities;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Departamento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



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