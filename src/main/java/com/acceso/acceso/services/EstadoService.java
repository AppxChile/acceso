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

    public Estado crearEstado(String nombre) {

        Optional<Estado> estadoExistente = estadoRepository.findByNombre(nombre);
        if (estadoExistente.isPresent()) {
            throw new IllegalArgumentException("El estado ya existe");
        }

        Estado estado = new Estado();
        estado.setNombre(nombre);
        return estadoRepository.save(estado);
    }

}
