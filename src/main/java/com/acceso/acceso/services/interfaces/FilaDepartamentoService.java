package com.acceso.acceso.services.interfaces;

import java.util.List;

import com.acceso.acceso.dto.FilaDto;
import com.acceso.acceso.entities.Departamento;

public interface FilaDepartamentoService {

    List<FilaDto> getByDepartamento(Departamento departamento);



}
