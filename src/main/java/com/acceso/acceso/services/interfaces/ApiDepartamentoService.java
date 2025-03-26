package com.acceso.acceso.services.interfaces;

import java.util.List;

import com.acceso.acceso.dto.DepartamentoResponse;
import com.acceso.acceso.dto.ListDepartamentosDto;

public interface ApiDepartamentoService {

    List<ListDepartamentosDto> getDepartamentos();

    DepartamentoResponse getDepartamentoById(Long id);

}
