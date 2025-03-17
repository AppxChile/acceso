package com.acceso.acceso.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

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

    @Query("SELECT d.nombre, COUNT(i) FROM IngresoDepartamento id " +
            "JOIN id.departamento d " +
            "JOIN id.ingreso i " +
            "WHERE i.horaIngreso BETWEEN :fechaInicio AND :fechaFin " +
            "GROUP BY d.nombre")
    List<Object[]> findTotalIngresosByDepartamentoBetweenDates(LocalDateTime fechaInicio, LocalDateTime fechaFin);

}
