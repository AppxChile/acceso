package com.acceso.acceso.services.interfaces;

import com.acceso.acceso.dto.IngresoDto;
import com.acceso.acceso.dto.IngresoRequest;
import com.acceso.acceso.entities.Ingreso;

public interface IngresoService {

    IngresoDto createIngreso(IngresoRequest request);

    Ingreso save(Ingreso ingreso);

    Ingreso findTopByRutOrderByHoraIngresoDesc(Integer rut);

}
