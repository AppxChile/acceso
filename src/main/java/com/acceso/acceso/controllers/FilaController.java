package com.acceso.acceso.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.acceso.acceso.dto.FilaDto;
import com.acceso.acceso.dto.FilaResponse;
import com.acceso.acceso.services.interfaces.FilaService;

@RestController
@CrossOrigin(origins = "https://dev.appx.cl/")
@RequestMapping("/api/acceso/filas")
public class FilaController {

    private final FilaService filaService;

    public FilaController(FilaService filaService) {
        this.filaService = filaService;
    }

    @GetMapping("/departamento/{id}")
    public ResponseEntity<List<FilaDto>> obtenerFilasPorDepartamento(@PathVariable Long id) {
        return ResponseEntity.ok(filaService.getFilasByDepartamento(id));
    }

    @PostMapping("/asignar")
    public ResponseEntity<Object> assignFila(@RequestParam Long id, @RequestParam String login,
            @RequestParam Long moduloId) {

        try {

            FilaResponse filaResponse = filaService.assignIngreso(id, login, moduloId);

            return ResponseEntity.status(HttpStatus.CREATED).body(filaResponse);

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    @PostMapping("/desasiginar")
    public void unassignFila(@RequestParam Long id) {

        try {

            filaService.unassignIngreso(id);

            ResponseEntity.status(HttpStatus.OK);

        } catch (Exception e) {

             ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    @PostMapping("/finalizar")
    public ResponseEntity<Object> finishFila(@RequestParam Long id) {

        try {

            FilaResponse filaResponse = filaService.finishIngreso(id);

            return ResponseEntity.status(HttpStatus.CREATED).body(filaResponse);

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

}
