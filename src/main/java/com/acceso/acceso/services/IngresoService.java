package com.acceso.acceso.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.acceso.acceso.dto.IngresoDto;
import com.acceso.acceso.dto.IngresoRequest;
import com.acceso.acceso.dto.PersonaDto;
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
            DepartamentoRepository departamentoRepository, IngresoDepartamentoRepository ingresoDepartamentoRepository,
            FilaRepository filaRepository, EstadoRepository estadoRepository) {
        this.ingresoRepository = ingresoRepository;
        this.personaRepository = personaRepository;
        this.departamentoRepository = departamentoRepository;
        this.ingresoDepartamentoRepository = ingresoDepartamentoRepository;
        this.filaRepository = filaRepository;
        this.estadoRepository = estadoRepository;
    }

    public IngresoDto crearIngreso(IngresoRequest request) {

        // Buscar o crear persona
        Persona persona = personaRepository.findByRut(request.getRut())
                .orElseGet(() -> {
                    Persona nuevaPersona = new Persona(request.getRut(), request.getSerie());
                    return personaRepository.save(nuevaPersona);
                });

        // Buscar departamentos por sus IDs
        Set<Departamento> departamentos = request.getIdDepartamentos().stream()
                .map(id -> departamentoRepository.findById(id)
                        .orElseThrow(
                                () -> new IllegalArgumentException("No se encontró el departamento con ID: " + id)))
                .collect(Collectors.toSet());

        // Crear ingreso
        Ingreso ingreso = new Ingreso();
        ingreso.setHoraIngreso(LocalDateTime.now());
        ingreso.setPersona(persona);
        ingreso = ingresoRepository.save(ingreso);

        // Asociar ingreso con cada departamento
        for (Departamento departamento : departamentos) {
            IngresoDepartamento ingresoDepartamento = new IngresoDepartamento();
            ingresoDepartamento.setIngreso(ingreso);
            ingresoDepartamento.setDepartamento(departamento);
            ingresoDepartamentoRepository.save(ingresoDepartamento);
        }

        // Obtener estado inicial
        Estado estadoInicial = estadoRepository.findByNombre("EN ESPERA")
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el estado inicial"));

        // Crear fila asociada al ingreso
        Fila fila = new Fila();
        fila.setHoraToma(LocalDateTime.now());
        fila.setIngreso(ingreso);
        fila.setEstado(estadoInicial);
        filaRepository.save(fila);

        // Convertir a DTO antes de devolver
        return convertirADTO(ingreso, estadoInicial);
    }

    private IngresoDto convertirADTO(Ingreso ingreso, Estado estado) {
        IngresoDto dto = new IngresoDto();
        dto.setId(ingreso.getId());
        dto.setHoraIngreso(ingreso.getHoraIngreso());

        // Mapear Persona a PersonaDTO
        PersonaDto personaDTO = new PersonaDto();
        personaDTO.setId(ingreso.getPersona().getId());
        personaDTO.setRut(ingreso.getPersona().getRut());
        personaDTO.setSerie(ingreso.getPersona().getSerie());
        dto.setPersona(personaDTO);

        // Mapear IDs de los departamentos
        List<Long> departamentos = Optional.ofNullable(ingreso.getIngresoDepartamentos())
                .orElse(List.of()) // Si es null, usa una lista vacía
                .stream()
                .map(ingresoDepartamento -> ingresoDepartamento.getDepartamento().getId())
                .toList();
        dto.setDepartamentos(departamentos);

        // Asignar el estado de la fila
        dto.setEstadoFila(estado.getNombre());

        return dto;
    }

}
