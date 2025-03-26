package com.acceso.acceso.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.acceso.acceso.entities.Estado;
import com.acceso.acceso.repositories.EstadoRepository;
import com.acceso.acceso.services.interfaces.EstadoService;

@Service
public class EstadoServiceImpl implements EstadoService {

    private final EstadoRepository estadoRepository;

    public EstadoServiceImpl(EstadoRepository estadoRepository) {
        this.estadoRepository = estadoRepository;
    }

    @Override
    public Estado createEstado(Estado estadoRequest) {
        Optional<Estado> estadoExistente = estadoRepository.findByNombre(estadoRequest.getNombre());
        if (estadoExistente.isPresent()) {
            throw new IllegalArgumentException("El estado ya existe");
        }

        Estado estado = new Estado();
        estado.setNombre(estadoRequest.getNombre());
        return estadoRepository.save(estado);
    }

    @Override
    public Estado findByNombre(String name) {
        return estadoRepository.findByNombre(name)
                .orElseThrow(() -> new IllegalArgumentException("Estado " + name + " no existe"));
    }

}
