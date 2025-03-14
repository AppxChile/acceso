package com.acceso.acceso.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.acceso.acceso.entities.Estado;

public interface EstadoRepository extends JpaRepository<Estado, Long> {

    Optional<Estado> findByNombre(String nombre);

}
