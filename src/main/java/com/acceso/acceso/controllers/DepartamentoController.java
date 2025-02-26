package com.acceso.acceso.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acceso.acceso.entities.Departamento;
import com.acceso.acceso.services.DepartamentoService;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/acceso/departamento")
public class DepartamentoController {

    private final DepartamentoService departamentoService;

    public DepartamentoController(DepartamentoService departamentoService) {
        this.departamentoService = departamentoService;
    }

    @PostMapping
    public Departamento crearDepartamento(@RequestBody Departamento departamento) {
        return departamentoService.creaDepartamento(departamento);
    }

}
