package com.acceso.acceso.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acceso.acceso.dto.ErrorResponse;
import com.acceso.acceso.entities.Salida;
import com.acceso.acceso.exceptions.MyExceptions;
import com.acceso.acceso.services.SalidaService;

@RestController
@CrossOrigin(origins = "https://dev.appx.cl/")
@RequestMapping("/api/acceso/salida")

public class SalidaController {

    private final SalidaService salidaService;

    public SalidaController(SalidaService salidaService){
        this.salidaService = salidaService;
    }

    @PostMapping("{rut}")
    public ResponseEntity<Object> createSalida(@PathVariable Integer rut){
        try {
            Salida salida = salidaService.createSalida(rut);
            return ResponseEntity.ok(salida);
        }catch(MyExceptions e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse("CONFLICT",e.getMessage()));
        
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
