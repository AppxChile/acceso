package com.acceso.acceso.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.acceso.acceso.dto.FilaDto;
import com.acceso.acceso.dto.PersonaResponse;
import com.acceso.acceso.entities.Departamento;
import com.acceso.acceso.entities.FilaDepartamento;
import com.acceso.acceso.repositories.FilaDepartamentoRepository;
import com.acceso.acceso.services.interfaces.ApiPersonaService;
import com.acceso.acceso.services.interfaces.FilaDepartamentoService;

@Service
public class FilaDepartamentoImpl implements FilaDepartamentoService {

    private final FilaDepartamentoRepository filaDepartamentoRepository;

    private final ApiPersonaService apiPersonaService;

    public FilaDepartamentoImpl(FilaDepartamentoRepository filaDepartamentoRepository,
            ApiPersonaService apiPersonaService) {
        this.filaDepartamentoRepository = filaDepartamentoRepository;
        this.apiPersonaService = apiPersonaService;
    }

    @Override
    public List<FilaDto> getByDepartamento(Departamento departamento) {

        List<FilaDepartamento> listFila = filaDepartamentoRepository.findByDepartamento(departamento);

        return listFila.stream().map(fila -> {

            PersonaResponse personaResponsa = apiPersonaService.getPersonaInfo(fila.getRutIngreso());
            FilaDto dto = new FilaDto();
            dto.setId(fila.getFilaId());
            dto.setHoraToma(fila.getHoraToma());
            dto.setEstado(fila.getEstadoNombre());
            dto.setIngresoId(fila.getIngresoId());
            dto.setModulo(fila.getModuloNombre());
            dto.setHoraIngreso(fila.getHoraIngresoFila());
            dto.setIdModulo(fila.getModuloId());

            dto.setNombre(personaResponsa.getNombreCompleto().toUpperCase());

            return dto;

        }).toList();

    }

}
