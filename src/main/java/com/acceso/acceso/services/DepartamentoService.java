package com.acceso.acceso.services;

import org.springframework.stereotype.Service;

import com.acceso.acceso.entities.Departamento;
import com.acceso.acceso.repositories.DepartamentoRepository;

@Service
public class DepartamentoService {

    private final DepartamentoRepository departamentoRepository;

    public DepartamentoService(DepartamentoRepository departamentoRepository) {
        this.departamentoRepository = departamentoRepository;
    }

    public Departamento creaDepartamento(Departamento departamento) {

        return departamentoRepository.save(departamento);
    }

}
