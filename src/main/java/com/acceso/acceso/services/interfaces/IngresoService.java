package com.acceso.acceso.services.interfaces;

import java.time.LocalDateTime;
import java.util.List;

import com.acceso.acceso.dto.IngresoByDeptosDates;
import com.acceso.acceso.dto.IngresoDto;
import com.acceso.acceso.dto.IngresoRequest;
import com.acceso.acceso.dto.IngresoWithouSalidaDto;
import com.acceso.acceso.dto.IngresosByFechasDto;
import com.acceso.acceso.dto.IngresosByHorasDto;
import com.acceso.acceso.entities.Ingreso;
import com.acceso.acceso.entities.Persona;

public interface IngresoService {

    IngresoDto createIngreso(IngresoRequest request);

    List<IngresosByFechasDto> getIngresosBetweenDates(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    List<IngresoWithouSalidaDto> getIngresoSalidaNull();

    List<IngresoByDeptosDates> getIngresosByDeptoBetweenDate(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    List<IngresosByHorasDto> getIngresosDayByHour();

    Ingreso save(Ingreso ingreso);

    Ingreso findTopByPersonaOrderByHoraIngresoDesc(Persona persona);

}
