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
import com.acceso.acceso.entities.Salida;
import com.acceso.acceso.exceptions.MyExceptions;
import com.acceso.acceso.repositories.DepartamentoRepository;
import com.acceso.acceso.repositories.EstadoRepository;
import com.acceso.acceso.repositories.FilaRepository;
import com.acceso.acceso.repositories.IngresoDepartamentoRepository;
import com.acceso.acceso.repositories.IngresoRepository;
import com.acceso.acceso.repositories.PersonaRepository;
import com.acceso.acceso.repositories.SalidaRepository;

@Service
public class IngresoService {

    private final IngresoRepository ingresoRepository;

    private final PersonaRepository personaRepository;

    private final DepartamentoRepository departamentoRepository;

    private final IngresoDepartamentoRepository ingresoDepartamentoRepository;

    private final FilaRepository filaRepository;

    private final EstadoRepository estadoRepository;

    private final SalidaRepository salidaRepository;

    public IngresoService(IngresoRepository ingresoRepository, PersonaRepository personaRepository,
            DepartamentoRepository departamentoRepository, IngresoDepartamentoRepository ingresoDepartamentoRepository,
            FilaRepository filaRepository, EstadoRepository estadoRepository,
            SalidaRepository salidaRepository) {
        this.ingresoRepository = ingresoRepository;
        this.personaRepository = personaRepository;
        this.departamentoRepository = departamentoRepository;
        this.ingresoDepartamentoRepository = ingresoDepartamentoRepository;
        this.filaRepository = filaRepository;
        this.estadoRepository = estadoRepository;
        this.salidaRepository = salidaRepository;
    }

    public IngresoDto createIngreso(IngresoRequest request) {

        Persona persona = getOrCreatePersona(request.getRut(), request.getSerie());

        if (hasIngresoWithoutSalida(persona)) {
            throw new MyExceptions("Persona no tiene registrada una salida");
        }

        Set<Departamento> departamentos = request.getIdDepartamentos().stream()
                .map(id -> departamentoRepository.findById(id)
                        .orElseThrow(
                                () -> new IllegalArgumentException("No se encontró el departamento con ID: " + id)))
                .collect(Collectors.toSet());

        Ingreso ingreso = new Ingreso();
        ingreso.setHoraIngreso(LocalDateTime.now());
        ingreso.setPersona(persona);

        ingreso = ingresoRepository.save(ingreso);

        for (Departamento departamento : departamentos) {
            IngresoDepartamento ingresoDepartamento = new IngresoDepartamento();
            ingresoDepartamento.setIngreso(ingreso);
            ingresoDepartamento.setDepartamento(departamento);
            ingresoDepartamentoRepository.save(ingresoDepartamento);
        }

        Estado estadoInicial = estadoRepository.findByNombre("EN ESPERA")
                .orElseThrow(() -> new IllegalArgumentException("No se encontró el estado inicial"));

        Fila fila = new Fila();

        fila.setIngreso(ingreso);
        fila.setEstado(estadoInicial);
        filaRepository.save(fila);

        return convertDTO(ingreso, estadoInicial);
    }

    private IngresoDto convertDTO(Ingreso ingreso, Estado estado) {
        IngresoDto dto = new IngresoDto();
        dto.setId(ingreso.getId());
        dto.setHoraIngreso(ingreso.getHoraIngreso());

        PersonaDto personaDTO = new PersonaDto();
        personaDTO.setId(ingreso.getPersona().getId());
        personaDTO.setRut(ingreso.getPersona().getRut());
        personaDTO.setSerie(ingreso.getPersona().getSerie());
        dto.setPersona(personaDTO);

        List<Long> departamentos = Optional.ofNullable(ingreso.getIngresoDepartamentos())
                .orElse(List.of())
                .stream()
                .map(ingresoDepartamento -> ingresoDepartamento.getDepartamento().getId())
                .toList();
        dto.setDepartamentos(departamentos);

        dto.setEstadoFila(estado.getNombre());

        return dto;
    }

    private Persona getOrCreatePersona(Integer rut, String serie) {

        return personaRepository.findByRut(rut)
                .orElseGet(() -> {
                    Persona nuevaPersona = new Persona(rut, serie);
                    return personaRepository.save(nuevaPersona);
                });
    }

    private boolean hasIngresoWithoutSalida(Persona persona) {
        Optional<Ingreso> optUltimoIngreso = ingresoRepository.findTopByPersonaOrderByHoraIngresoDesc(persona);

        if (optUltimoIngreso.isEmpty()) {
            return false;
        }

        Ingreso ultimoIngreso = optUltimoIngreso.get();

        Optional<Salida> optSalida = salidaRepository.findByIngreso(ultimoIngreso);

        return optSalida.isEmpty();
    }


   
}
