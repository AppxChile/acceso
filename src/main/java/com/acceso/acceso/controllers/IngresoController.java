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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.acceso.acceso.dto.ErrorResponse;
import com.acceso.acceso.dto.IngresoByDeptosDates;
import com.acceso.acceso.dto.IngresoDto;
import com.acceso.acceso.dto.IngresoRequest;
import com.acceso.acceso.dto.IngresoWithouSalidaDto;
import com.acceso.acceso.dto.IngresosByFechasDto;
import com.acceso.acceso.dto.IngresosByHorasDto;
import com.acceso.acceso.exceptions.MyExceptions;
import com.acceso.acceso.services.interfaces.IngresoQueryService;
import com.acceso.acceso.services.interfaces.IngresoService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "https://dev.appx.cl/")
@RequestMapping("/api/acceso/ingreso")
public class IngresoController {

    private final IngresoService ingresoService;

    private final IngresoQueryService ingresoQueryService;

    public IngresoController(IngresoService ingresoService, IngresoQueryService ingresoQueryService) {
        this.ingresoService = ingresoService;
        this.ingresoQueryService=ingresoQueryService;
    }

    @PostMapping
    public ResponseEntity<Object> createIngreso(HttpServletRequest request, @RequestBody IngresoRequest ingresoRequest) {
        try {

         
            IngresoDto ingreso = ingresoService.createIngreso(ingresoRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(ingreso);

        } catch (MyExceptions e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse("CONFLICT", e.getMessage()));

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("BAD_REQUEST", e.getMessage()));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("INTERNAL_SERVER_ERROR", e.getMessage()));
        }
    }

    @GetMapping("/byFechas")
    public ResponseEntity<Object> getIngresosBetweenDAte(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate fechaFin) {

        LocalDateTime inicioDelDia = fechaInicio.atStartOfDay(); // 00:00:00 del día
        LocalDateTime finDelDia = fechaFin.atTime(LocalTime.MAX); // 23:59:59.999999 del dí
        try {
            List<IngresosByFechasDto> ingresoList = ingresoQueryService.getIngresosBetweenDates(inicioDelDia, finDelDia);
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

    @GetMapping("ingresos-sin-salida")
    public ResponseEntity<List<IngresoWithouSalidaDto>> getIngresosSalidaNull() {
        try {
            List<IngresoWithouSalidaDto> ingresos = ingresoQueryService.getIngresoSalidaNull();
            return ResponseEntity.ok(ingresos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/por-departamento")
    public ResponseEntity<Object> getIngresosByDepto(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        try {
            List<IngresoByDeptosDates> ingresos = ingresoQueryService.getIngresosByDeptoBetweenDate(
                    fechaInicio.atStartOfDay(), fechaFin.atTime(23, 59, 59));
            return ResponseEntity.ok(ingresos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

    @GetMapping("/por-dia")
    public ResponseEntity<List<IngresosByHorasDto>> getIngresosByHourDay() {
        try {
            List<IngresosByHorasDto> ingresos = ingresoQueryService.getIngresosDayByHour();
            return ResponseEntity.ok(ingresos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

   
}
