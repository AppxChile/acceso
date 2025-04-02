package com.acceso.acceso.dto;

import java.util.HashSet;
import java.util.Set;


public class UbicacionDto {

    private String nombreUbicacion;

    private Set<ListDepartamentosDto> departamentos = new HashSet<>();

    public String getNombreUbicacion() {
        return nombreUbicacion;
    }

    public void setNombreUbicacion(String nombreUbicacion) {
        this.nombreUbicacion = nombreUbicacion;
    }

    public Set<ListDepartamentosDto> getDepartamentos() {
        return departamentos;
    }

    public void setDepartamentos(Set<ListDepartamentosDto> departamentos) {
        this.departamentos = departamentos;
    }

  
    

}
