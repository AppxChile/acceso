package com.acceso.acceso.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acceso.acceso.dto.FilaDto;
import com.acceso.acceso.services.FilaService;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/filas")
public class FilaController {

    private final FilaService filaService;

    public FilaController(FilaService filaService) {
        this.filaService = filaService;
    }

    @GetMapping("/departamento/{id}")
    public ResponseEntity<List<FilaDto>> obtenerFilasPorDepartamento(@PathVariable Long id) {
        return ResponseEntity.ok(filaService.obtenerFilasPorDepartamento(id));
    }

}
