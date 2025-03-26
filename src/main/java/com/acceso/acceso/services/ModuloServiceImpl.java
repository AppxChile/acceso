package com.acceso.acceso.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.acceso.acceso.entities.Modulo;
import com.acceso.acceso.repositories.ModuloRepository;
import com.acceso.acceso.services.interfaces.ModuloService;

@Service
public class ModuloServiceImpl implements ModuloService {

    private final ModuloRepository moduloRepository;

    public ModuloServiceImpl(ModuloRepository moduloRepository) {
        this.moduloRepository = moduloRepository;
    }

    @Override
    public List<Modulo> getAll() {
        return moduloRepository.findAll();
    }

    @Override
    public Modulo findById(Long id) {
        return moduloRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Modulo " + id + " no existe"));
    }

}
