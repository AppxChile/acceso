package com.acceso.acceso.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.acceso.acceso.entities.Persona;

public interface PersonaRepository extends JpaRepository<Persona, Long> {

    Optional<Persona> findByRut(Integer rut);

}
