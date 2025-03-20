package com.acceso.acceso.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.acceso.acceso.dto.ListDepartamentosDto;

@Service
public class DepartamentoService {


    private final ApiService apiService;

    public DepartamentoService(ApiService apiService) {
        this.apiService=apiService;
    }

  
    public List<ListDepartamentosDto> findAll() {

       return apiService.getDepartamentos();

}
}
