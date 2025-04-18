package com.acceso.acceso.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acceso.acceso.dto.ListDepartamentosDto;
import com.acceso.acceso.services.DepartamentoService;

@RestController
@CrossOrigin(origins = "https://dev.appx.cl/")
@RequestMapping("/api/acceso/departamento")
public class DepartamentoController {

    private final DepartamentoService departamentoService;

    public DepartamentoController(DepartamentoService departamentoService) {
        this.departamentoService = departamentoService;
    }


    @GetMapping("/list")
    public List<ListDepartamentosDto> getDepartamentos() {
        return departamentoService.findAll();
    }

}
