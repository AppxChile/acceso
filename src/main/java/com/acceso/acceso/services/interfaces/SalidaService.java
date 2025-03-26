package com.acceso.acceso.services.interfaces;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import com.acceso.acceso.dto.SalidasByFechasDto;
import com.acceso.acceso.entities.Ingreso;
import com.acceso.acceso.entities.Salida;

public interface SalidaService {

     Salida createSalida(Integer rut);

     List<SalidasByFechasDto> getSalidasBetweenDates(LocalDateTime fechaInicio, LocalDateTime fechaFin);

     Salida save(Salida salida);

     Salida findById(Long id);

     Optional<Salida> findByIngreso(Ingreso ingreso);

}
