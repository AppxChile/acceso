package com.acceso.acceso.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.acceso.acceso.entities.Estado;
import com.acceso.acceso.services.EstadoService;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class EstadoController {

    private final EstadoService estadoService;

    public EstadoController(EstadoService estadoService) {
        this.estadoService = estadoService;
    }

      @PostMapping
    public Estado crearDepartamento(@RequestBody Estado estado) {
        return estadoService.crearEstado(estado);
    }

}
