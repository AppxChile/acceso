package com.acceso.acceso.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.acceso.acceso.dto.FilaDto;
import com.acceso.acceso.dto.PersonaResponse;
import com.acceso.acceso.entities.Fila;
import com.acceso.acceso.repositories.FilaRepository;

@Service
public class FilaService {

     private final FilaRepository filaRepository;

     private final ApiService apiService;

    public FilaService(FilaRepository filaRepository, ApiService apiService) {
        this.filaRepository = filaRepository;
        this.apiService= apiService;
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


        PersonaResponse persona = apiService.obtenerDatos(fila.getIngreso().getPersona().getRut());

        dto.setNombre(persona.getNombres().concat(" ").concat(persona.getPaterno().concat(" ").concat(persona.getMaterno())));
        return dto;
    }

}
