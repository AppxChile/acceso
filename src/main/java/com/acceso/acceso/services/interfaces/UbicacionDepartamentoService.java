package com.acceso.acceso.services.interfaces;

import java.util.List;

import com.acceso.acceso.dto.UbicacionDto;
import com.acceso.acceso.entities.UbicacionDepartamento;

public interface UbicacionDepartamentoService {

    List<UbicacionDepartamento> findAll();

    UbicacionDto getUbicaciones(Long id);

}
