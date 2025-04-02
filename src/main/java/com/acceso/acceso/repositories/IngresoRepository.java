package com.acceso.acceso.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.acceso.acceso.entities.Ingreso;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IngresoRepository extends JpaRepository<Ingreso, Long> {


    Optional<Ingreso> findTopByRutOrderByHoraIngresoDesc(Integer rut);

    List<Ingreso> findByhoraIngresoBetween(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    List<Ingreso> findBySalidaIsNull();

    @Query("SELECT d.id, COUNT(i), DATE_FORMAT(i.horaIngreso, '%Y-%m-%d') FROM IngresoDepartamento id " +
    
            "JOIN id.departamento d " +
            "JOIN id.ingreso i " +
            "WHERE i.horaIngreso BETWEEN :fechaInicio AND :fechaFin " +
            "GROUP BY d.id, DATE_FORMAT(i.horaIngreso, '%Y-%m-%d')")
    List<Object[]> findTotalIngresosByDepartamentoBetweenDates(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    @Query(value = "SELECT HOUR(hora_ingreso) AS hora, " +
            "COUNT(*) AS total, " +
            "DATE_FORMAT(hora_ingreso, '%Y-%m-%d') AS fecha " +
            "FROM ingreso " +
            "WHERE DATE(hora_ingreso) = CURRENT_DATE() " +
            "GROUP BY HOUR(hora_ingreso), DATE_FORMAT(hora_ingreso, '%Y-%m-%d')", nativeQuery = true)
    List<Object[]> findIngresosByHour();
    

    

}
