package com.acceso.acceso.services.interfaces;

import java.time.LocalDateTime;
import java.util.List;

import com.acceso.acceso.dto.IngresoByDeptosDates;
import com.acceso.acceso.dto.IngresoWithouSalidaDto;
import com.acceso.acceso.dto.IngresosByFechasDto;
import com.acceso.acceso.dto.IngresosByHorasDto;

public interface IngresoQueryService {

    List<IngresosByFechasDto> getIngresosBetweenDates(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    List<IngresoWithouSalidaDto> getIngresoSalidaNull();

    List<IngresoByDeptosDates> getIngresosByDeptoBetweenDate(LocalDateTime fechaInicio, LocalDateTime fechaFin);

     List<IngresosByHorasDto> getIngresosDayByHour();

}
