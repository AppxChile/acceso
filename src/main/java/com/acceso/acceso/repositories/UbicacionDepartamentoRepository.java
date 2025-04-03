package com.acceso.acceso.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.acceso.acceso.entities.UbicacionDepartamento;

import java.util.List;
import java.util.Optional;

public interface UbicacionDepartamentoRepository extends JpaRepository<UbicacionDepartamento, Long> {

    Optional<UbicacionDepartamento> findByNombreUbicacion(String nombreUbicacion);

    List<UbicacionDepartamento> findAll();

}
