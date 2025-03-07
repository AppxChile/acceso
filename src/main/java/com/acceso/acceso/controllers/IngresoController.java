package com.acceso.acceso.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acceso.acceso.dto.ErrorResponse;
import com.acceso.acceso.dto.IngresoDto;
import com.acceso.acceso.dto.IngresoRequest;
import com.acceso.acceso.exceptions.MyExceptions;
import com.acceso.acceso.services.IngresoService;

@RestController
@CrossOrigin(origins = "https://dev.appx.cl/")
@RequestMapping("/api/acceso/ingreso")
public class IngresoController {

    private final IngresoService ingresoService;

    public IngresoController(IngresoService ingresoService) {
        this.ingresoService = ingresoService;
    }

  @PostMapping
public ResponseEntity<Object> createIngreso(@RequestBody IngresoRequest request) {
    try {
        IngresoDto ingreso = ingresoService.createIngreso(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ingreso);
        
    } catch (MyExceptions e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse("CONFLICT", e.getMessage()));
        
    } catch (IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("BAD_REQUEST", e.getMessage()));
        
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("INTERNAL_SERVER_ERROR", e.getMessage()));
    }
}
}
