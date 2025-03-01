package com.acceso.acceso.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.acceso.acceso.controllers.UsuarioResponse;
import com.acceso.acceso.dto.FilaDto;
import com.acceso.acceso.dto.FilaResponse;
import com.acceso.acceso.dto.PersonaResponse;
import com.acceso.acceso.entities.Estado;
import com.acceso.acceso.entities.Fila;
import com.acceso.acceso.repositories.EstadoRepository;
import com.acceso.acceso.repositories.FilaRepository;

@Service
public class FilaService {

    private final FilaRepository filaRepository;

    private final ApiService apiService;

    private final EstadoRepository estadoRepository;

    public FilaService(FilaRepository filaRepository, ApiService apiService, EstadoRepository estadoRepository) {
        this.filaRepository = filaRepository;
        this.apiService = apiService;
        this.estadoRepository = estadoRepository;
    }

    public List<FilaDto> obtenerFilasPorDepartamento(Long departamentoId) {
        List<Fila> filas = filaRepository.findFilasByDepartamento(departamentoId);
        return filas.stream().map(this::convertirAFilaDto).toList();
    }

    private FilaDto convertirAFilaDto(Fila fila) {
        FilaDto dto = new FilaDto();
        dto.setId(fila.getId());
        dto.setHoraToma(fila.getHoraToma());
        dto.setEstado(fila.getEstado().getNombre());
        dto.setIngresoId(fila.getIngreso().getId());

        PersonaResponse persona = apiService.obtenerDatosPersona(fila.getIngreso().getPersona().getRut());

        dto.setNombre(
                persona.getNombres().concat(" ").concat(persona.getPaterno().concat(" ").concat(persona.getMaterno())));
        return dto;
    }

    public FilaResponse asignarFila(Long id, String login) {

        // Buscar la fila por ID
        Fila fila = filaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Fila con ID " + id + " no encontrada"));

        // Buscar el estado "EN ATENCION"
        Estado estado = estadoRepository.findByNombre("EN ATENCION")
                .orElseThrow(() -> new IllegalArgumentException("Estado 'EN ATENCION' no existe"));

        // Buscar usuario en la API
        UsuarioResponse usuarioResponse = apiService.obtenerUsuario(login);
        if (usuarioResponse == null) {
            throw new IllegalArgumentException("Usuario con login " + login + " no existe");
        }

        // Asignar valores a la fila
        fila.setAsignadoA(login);
        fila.setEstado(estado);

        // Guardar en la BD
        fila = filaRepository.save(fila);

        // Crear respuesta con los datos relevantes
        return new FilaResponse(fila.getId(), fila.getAsignadoA(), fila.getEstado().getNombre());
    }

}
