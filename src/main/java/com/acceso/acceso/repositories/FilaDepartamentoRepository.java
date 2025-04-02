package com.acceso.acceso.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import com.acceso.acceso.entities.Departamento;
import com.acceso.acceso.entities.Fila;
import com.acceso.acceso.entities.FilaDepartamento;
import java.util.List;



public interface FilaDepartamentoRepository extends JpaRepository<FilaDepartamento,Long> {

    List<FilaDepartamento> findByFila(Fila fila);

    List<FilaDepartamento> findByDepartamento(Departamento departamento);

}
