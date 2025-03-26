package com.acceso.acceso.services.interfaces;

import java.util.List;

import com.acceso.acceso.dto.FilaDto;
import com.acceso.acceso.dto.FilaResponse;
import com.acceso.acceso.entities.Fila;

public interface FilaService {

    List<FilaDto> getFilasByDepartamento(Long departamentoId);

    FilaDto convertFilaDto(Fila fila);

    FilaResponse assignIngreso(Long id, String login, Long moduloId);

    void unassignIngreso(Long id);

    FilaResponse finishIngreso(Long id);

    Fila save(Fila fila);

}
