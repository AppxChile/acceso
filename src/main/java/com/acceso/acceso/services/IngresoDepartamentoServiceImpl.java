package com.acceso.acceso.services;

import org.springframework.stereotype.Service;

import com.acceso.acceso.entities.IngresoDepartamento;
import com.acceso.acceso.repositories.IngresoDepartamentoRepository;
import com.acceso.acceso.services.interfaces.IngresoDepartamentoService;

@Service
public class IngresoDepartamentoServiceImpl implements IngresoDepartamentoService {

    private final IngresoDepartamentoRepository ingresoDepartamentoRepository;

    public IngresoDepartamentoServiceImpl(IngresoDepartamentoRepository ingresoDepartamentoRepository) {
        this.ingresoDepartamentoRepository = ingresoDepartamentoRepository;
    }

    @Override
    public IngresoDepartamento save(IngresoDepartamento ingresoDepartamento) {
        return ingresoDepartamentoRepository.save(ingresoDepartamento);
    }

}
