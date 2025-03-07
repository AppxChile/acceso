package com.acceso.acceso.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.acceso.acceso.entities.Ingreso;
import com.acceso.acceso.entities.Salida;


public interface SalidaRepository extends JpaRepository<Salida,Long> {


    Optional<Salida> findByIngreso(Ingreso ingreso);

    Optional<Salida> findByIngresoId(Long id);

}
