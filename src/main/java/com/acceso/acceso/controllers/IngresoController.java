package com.acceso.acceso.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acceso.acceso.dto.IngresoDto;
import com.acceso.acceso.dto.IngresoRequest;
import com.acceso.acceso.services.IngresoService;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/acceso/ingreso")
public class IngresoController {

    private final IngresoService ingresoService;

    public IngresoController(IngresoService ingresoService) {
        this.ingresoService = ingresoService;
    }

    @PostMapping
    public ResponseEntity<Object> crearIngreso(@RequestBody IngresoRequest request) {
        try {
            IngresoDto ingreso = ingresoService.crearIngreso(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(ingreso);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
