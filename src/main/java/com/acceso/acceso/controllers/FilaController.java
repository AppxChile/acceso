package com.acceso.acceso.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acceso.acceso.dto.AssignRequest;
import com.acceso.acceso.dto.FilaDto;
import com.acceso.acceso.dto.FilaResponse;
import com.acceso.acceso.dto.FinishRequest;
import com.acceso.acceso.dto.UnassigRequest;
import com.acceso.acceso.entities.Departamento;
import com.acceso.acceso.repositories.DepartamentoRepository;
import com.acceso.acceso.services.interfaces.FilaDepartamentoService;
import com.acceso.acceso.services.interfaces.FilaService;

@RestController
@CrossOrigin(origins = "https://dev.appx.cl/")
@RequestMapping("/api/acceso/filas")
public class FilaController {

    private final FilaService filaService;

    private final FilaDepartamentoService filaDepartamentoService;

    private final DepartamentoRepository departamentoRepository;

    public FilaController(FilaService filaService, DepartamentoRepository departamentoRepository,
            FilaDepartamentoService filaDepartamentoService) {
        this.filaService = filaService;
        this.departamentoRepository = departamentoRepository;
        this.filaDepartamentoService = filaDepartamentoService;
    }

    @PostMapping("/asignar")
    public ResponseEntity<Object> assignFila(@RequestBody AssignRequest request) {

        try {

            FilaResponse filaResponse = filaService.assignIngreso(request);

            return ResponseEntity.status(HttpStatus.CREATED).body(filaResponse);

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    @PostMapping("/desasiginar")
    public void unassignFila(@RequestBody UnassigRequest request) {

        try {

            filaService.unassignIngreso(request);

            ResponseEntity.status(HttpStatus.OK);

        } catch (Exception e) {

            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    @PostMapping("/finalizar")
    public ResponseEntity<Object> finishFila(@RequestBody FinishRequest request) {

        try {

            FilaResponse filaResponse = filaService.finishIngreso(request);

            return ResponseEntity.status(HttpStatus.CREATED).body(filaResponse);

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }

    @GetMapping("/byDepto/{id}")
    public ResponseEntity<Object> getFilasByDepto(@PathVariable Long id) {
        try {
            Departamento departamento = getDeptoById(id);

            List<FilaDto> response = filaDepartamentoService.getByDepartamento(departamento);

            if (response.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    private Departamento getDeptoById(Long id) {
        return departamentoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe el depto " + id));
    }

}
