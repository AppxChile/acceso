package com.acceso.acceso.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.acceso.acceso.dto.ListDepartamentosDto;
import com.acceso.acceso.entities.Departamento;
import com.acceso.acceso.repositories.DepartamentoRepository;

@Service
public class DepartamentoService {

    private final DepartamentoRepository departamentoRepository;

    public DepartamentoService(DepartamentoRepository departamentoRepository) {
        this.departamentoRepository = departamentoRepository;
    }

    public Departamento createDepartamento(Departamento departamento) {

        return departamentoRepository.save(departamento);
    }

    public List<ListDepartamentosDto> findAll() {

        List<Departamento> deptos = departamentoRepository.findAll();

        return deptos.stream().map(depto -> {

            ListDepartamentosDto dto = new ListDepartamentosDto();

            dto.setId(depto.getId());
            dto.setNombreDepartamento(depto.getNombre());

            return dto;
        }).toList();
    }

}
