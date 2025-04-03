package com.acceso.acceso.services.interfaces;

import java.util.List;

import com.acceso.acceso.dto.UbicacionDto;
import com.acceso.acceso.dto.UbicacionResponse;

public interface UbicacionDepartamentoService {

    List<UbicacionResponse> findAll();

    UbicacionDto getUbicaciones(Long id);

   

}
