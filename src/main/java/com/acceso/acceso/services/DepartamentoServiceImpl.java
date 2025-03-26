package com.acceso.acceso.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.acceso.acceso.dto.DepartamentoResponse;
import com.acceso.acceso.dto.ListDepartamentosDto;
import com.acceso.acceso.services.interfaces.ApiDepartamentoService;
import com.acceso.acceso.services.interfaces.DepartamentoService;

@Service
public class DepartamentoServiceImpl implements DepartamentoService {

  private final ApiDepartamentoService apiDepartamentoService;

    public DepartamentoServiceImpl(ApiDepartamentoService apiDepartamentoService) {
        this.apiDepartamentoService = apiDepartamentoService;
    }

    @Override
    public List<ListDepartamentosDto> findAll() {
       return apiDepartamentoService.getDepartamentos();
    }

    @Override
    public DepartamentoResponse findById(Long id) {
        return apiDepartamentoService.getDepartamentoById(id);
    }

}
