package com.acceso.acceso.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.acceso.acceso.entities.Fila;

public interface FilaRepository extends JpaRepository<Fila, Long> {

    @Query("SELECT f FROM Fila f JOIN f.ingreso i JOIN i.ingresoDepartamentos id WHERE id.departamento.id = :departamentoId")
    List<Fila> findFilasByDepartamento(@Param("departamentoId") Long departamentoId);

    

}
