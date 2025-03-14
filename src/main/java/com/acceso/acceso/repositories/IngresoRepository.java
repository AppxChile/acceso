package com.acceso.acceso.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.acceso.acceso.entities.Ingreso;
import com.acceso.acceso.entities.Persona;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IngresoRepository extends JpaRepository<Ingreso, Long> {

    Optional<Ingreso> findByPersona(Persona persona);

    Optional<Ingreso> findTopByPersonaOrderByHoraIngresoDesc(Persona persona);

    List<Ingreso> findByhoraIngresoBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    List<Ingreso> findBySalidaIsNull();

}
