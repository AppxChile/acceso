package com.acceso.acceso.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.acceso.acceso.entities.Ingreso;
import com.acceso.acceso.entities.Persona;

import java.time.LocalDateTime;
import java.util.Optional;


public interface IngresoRepository extends JpaRepository<Ingreso,Long> {

    Optional<Ingreso>  findByPersona(Persona persona);

    Optional<Ingreso> findTopByPersonaOrderByHoraIngresoDesc(Persona persona);

    Optional<Ingreso> findByhoraIngresoBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin );

}
