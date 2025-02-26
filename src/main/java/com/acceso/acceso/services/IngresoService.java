package com.acceso.acceso.services;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.acceso.acceso.dto.IngresoRequest;
import com.acceso.acceso.entities.Departamento;
import com.acceso.acceso.entities.Estado;
import com.acceso.acceso.entities.Fila;
import com.acceso.acceso.entities.Ingreso;
import com.acceso.acceso.entities.IngresoDepartamento;
import com.acceso.acceso.entities.Persona;
import com.acceso.acceso.repositories.DepartamentoRepository;
import com.acceso.acceso.repositories.EstadoRepository;
import com.acceso.acceso.repositories.FilaRepository;
import com.acceso.acceso.repositories.IngresoDepartamentoRepository;
import com.acceso.acceso.repositories.IngresoRepository;
import com.acceso.acceso.repositories.PersonaRepository;

@Service
public class IngresoService {

    private final IngresoRepository ingresoRepository;

    private final PersonaRepository personaRepository;

    private final DepartamentoRepository departamentoRepository;

    private final IngresoDepartamentoRepository ingresoDepartamentoRepository;

    private final FilaRepository filaRepository;

    private final EstadoRepository estadoRepository;

    public IngresoService(IngresoRepository ingresoRepository, PersonaRepository personaRepository,
            DepartamentoRepository departamentoRepository,IngresoDepartamentoRepository ingresoDepartamentoRepository,
            FilaRepository filaRepository, EstadoRepository estadoRepository) {
        this.ingresoRepository = ingresoRepository;
        this.personaRepository = personaRepository;
        this.departamentoRepository = departamentoRepository;
        this.ingresoDepartamentoRepository=ingresoDepartamentoRepository;
        this.filaRepository=filaRepository;
        this.estadoRepository= estadoRepository;
    }

   public Ingreso crearIngreso(IngresoRequest request) {

    Persona persona = personaRepository.findByRut(request.getRut())
        .orElseGet(() -> {
            Persona nuevaPersona = new Persona(request.getRut(), request.getSerie());
            return personaRepository.save(nuevaPersona);  // 🔹 Guardamos y retornamos la nueva persona
        });

    Departamento departamento = departamentoRepository.findByNombre(request.getDepartamento())
        .orElseThrow(() -> new IllegalArgumentException("No se encontró el departamento"));

    Ingreso ingreso = new Ingreso();
    ingreso.setHoraIngreso(request.getFechaHora());
    ingreso.setPersona(persona); 
    ingreso = ingresoRepository.save(ingreso); 

    // Asociar ingreso con departamento
    IngresoDepartamento ingresoDepartamento = new IngresoDepartamento();
    ingresoDepartamento.setIngreso(ingreso);
    ingresoDepartamento.setDepartamento(departamento);
    ingresoDepartamentoRepository.save(ingresoDepartamento);

    // Buscar estado inicial (Ejemplo: "En espera")
    Estado estadoInicial = estadoRepository.findByNombre("En espera")
        .orElseThrow(() -> new IllegalArgumentException("No se encontró el estado inicial"));

    // Crear y guardar fila
    Fila fila = new Fila();
    fila.setHoraToma(LocalDateTime.now());
    fila.setIngreso(ingreso);
    fila.setEstado(estadoInicial);
    filaRepository.save(fila);

    return ingreso;
}


}
