package com.acceso.acceso.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.acceso.acceso.entities.Estado;
import com.acceso.acceso.repositories.EstadoRepository;

@Service
public class EstadoService {

    private final EstadoRepository estadoRepository;

    public EstadoService(EstadoRepository estadoRepository) {
        this.estadoRepository = estadoRepository;
    }

    public Estado crearEstado(Estado estadoRequest) {

        Optional<Estado> estadoExistente = estadoRepository.findByNombre(estadoRequest.getNombre());
        if (estadoExistente.isPresent()) {
            throw new IllegalArgumentException("El estado ya existe");
        }

        Estado estado = new Estado();
        estado.setNombre(estadoRequest.getNombre());
        return estadoRepository.save(estado);
    }

}
