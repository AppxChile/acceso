package com.acceso.acceso.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acceso.acceso.dto.ListDepartamentosDto;
import com.acceso.acceso.dto.UbicacionDto;
import com.acceso.acceso.dto.UbicacionResponse;
import com.acceso.acceso.services.interfaces.DepartamentoService;
import com.acceso.acceso.services.interfaces.UbicacionDepartamentoService;

@RestController
@CrossOrigin(origins = "https://dev.appx.cl/")
@RequestMapping("/api/acceso/departamento")
public class DepartamentoController {

    private final DepartamentoService departamentoService;

    private final UbicacionDepartamentoService ubicacionDepartamentoService;


    public DepartamentoController(DepartamentoService departamentoService,UbicacionDepartamentoService ubicacionDepartamentoService) {
        this.departamentoService = departamentoService;
        this.ubicacionDepartamentoService=ubicacionDepartamentoService;
    }


    @GetMapping("/list")
    public List<ListDepartamentosDto> getDepartamentos() {
        return departamentoService.findAll();
    }

    @GetMapping("/ubicaciones/{id}")
    public ResponseEntity<UbicacionDto> getUbicacionesById(@PathVariable Long id) {
        UbicacionDto ubicacion = ubicacionDepartamentoService.getUbicaciones(id);
        return ResponseEntity.ok(ubicacion);
    }

    @GetMapping("/ubicaciones/list")
    public ResponseEntity<List<UbicacionResponse>> getUbicaciones() {
        List<UbicacionResponse> ubicacion = ubicacionDepartamentoService.findAll();
        return ResponseEntity.ok(ubicacion);
    }



}
