package com.acceso.acceso.controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.acceso.acceso.dto.ErrorResponse;
import com.acceso.acceso.dto.SalidasByFechasDto;
import com.acceso.acceso.entities.Salida;
import com.acceso.acceso.exceptions.MyExceptions;
import com.acceso.acceso.services.interfaces.SalidaService;

@RestController
@CrossOrigin(origins = "https://dev.appx.cl/")
@RequestMapping("/api/acceso/salida")

public class SalidaController {

    private final SalidaService salidaService;

    public SalidaController(SalidaService salidaService) {
        this.salidaService = salidaService;
    }

    @PostMapping("{rut}")
    public ResponseEntity<Object> createSalida(@PathVariable Integer rut) {
        try {
            Salida salida = salidaService.createSalida(rut);
            return ResponseEntity.ok(salida);
        } catch (MyExceptions e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse("CONFLICT", e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/byFechas")
    public ResponseEntity<Object> getIngresosBetweenDAte(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fechaFin) {

        LocalDateTime inicioDelDia = fechaInicio.atStartOfDay();
        LocalDateTime finDelDia = fechaFin.atTime(LocalTime.MAX);
        try {
            List<SalidasByFechasDto> ingresoList = salidaService.getSalidasBetweenDates(inicioDelDia, finDelDia);
            return ResponseEntity.status(HttpStatus.CREATED).body(ingresoList);

        } catch (MyExceptions e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse("CONFLICT", e.getMessage()));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("BAD_REQUEST", e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("INTERNAL_SERVER_ERROR", e.getMessage()));
        }
    }

}
