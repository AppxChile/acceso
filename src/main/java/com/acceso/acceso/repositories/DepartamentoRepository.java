package com.acceso.acceso.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.acceso.acceso.entities.Departamento;

public interface DepartamentoRepository extends JpaRepository<Departamento,Long>{

    Optional<Departamento> findByNombre(String nombre);

}
