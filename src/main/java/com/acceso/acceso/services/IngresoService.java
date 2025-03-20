package com.acceso.acceso.services;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.acceso.acceso.dto.IngresoByDeptosDates;
import com.acceso.acceso.dto.IngresoDto;
import com.acceso.acceso.dto.IngresoRequest;
import com.acceso.acceso.dto.IngresoWithouSalidaDto;
import com.acceso.acceso.dto.IngresosByFechasDto;
import com.acceso.acceso.dto.IngresosByHorasDto;
import com.acceso.acceso.dto.ListDepartamentosDto;
import com.acceso.acceso.dto.PersonaDto;
import com.acceso.acceso.dto.PersonaResponse;
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

    private final ApiService apiService;

    public IngresoService(IngresoRepository ingresoRepository, PersonaRepository personaRepository,
            DepartamentoRepository departamentoRepository, IngresoDepartamentoRepository ingresoDepartamentoRepository,
            FilaRepository filaRepository, EstadoRepository estadoRepository,
            SalidaRepository salidaRepository,
            ApiService apiService) {
        this.ingresoRepository = ingresoRepository;
        this.personaRepository = personaRepository;
        this.departamentoRepository = departamentoRepository;
        this.ingresoDepartamentoRepository = ingresoDepartamentoRepository;
        this.filaRepository = filaRepository;
        this.estadoRepository = estadoRepository;
        this.salidaRepository = salidaRepository;
        this.apiService = apiService;
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

        ingreso.setHoraIngreso(fechaHoraIngreso());
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

    private LocalDateTime fechaHoraIngreso() {
        ZoneId zonaChile = ZoneId.of("America/Santiago");
        return LocalDateTime.now(zonaChile);

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

    public List<IngresosByFechasDto> getIngresosBetweenDates(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        List<Ingreso> ingresos = ingresoRepository.findByhoraIngresoBetween(fechaInicio, fechaFin);

        return ingresos.stream().map(ingr -> {
            IngresosByFechasDto dto = new IngresosByFechasDto();
            dto.setFechaIngreso(ingr.getHoraIngreso());

            if (ingr.getPersona() != null) {
                PersonaResponse personaResponse = apiService.getPersonaInfo(ingr.getPersona().getRut());
                dto.setNombre(personaResponse != null ? personaResponse.getNombres().concat(" ")
                        .concat(personaResponse.getPaterno().concat(" ").concat(personaResponse.getMaterno()))
                        : "Desconocido");
                dto.setRut(personaResponse.getRut().toString().concat("-").concat(personaResponse.getVrut()));
            } else {
                dto.setNombre("Desconocido");
            }

            if (ingr.getSalida() != null) {
                dto.setFechaSalida(ingr.getSalida().getHoraSalida());
            } else {
                dto.setFechaSalida(null);
            }

            return dto;
        }).toList();
    }

    public List<IngresoWithouSalidaDto> getIngresoSalidaNull() {

        List<Ingreso> ingresos = ingresoRepository.findBySalidaIsNull();

        return ingresos.stream().map(ing -> {

            IngresoWithouSalidaDto dto = new IngresoWithouSalidaDto();

            PersonaResponse personaResponse = apiService.getPersonaInfo(ing.getPersona().getRut());

            dto.setRut(personaResponse.getRut().toString().concat("-").concat(personaResponse.getVrut()));
            dto.setNombre(personaResponse.getNombres().concat(" ")
                    .concat(personaResponse.getPaterno().concat(" ").concat(personaResponse.getMaterno())));
            dto.setHoraIngreso(ing.getHoraIngreso());

            return dto;

        }).toList();

    }

    public List<IngresoByDeptosDates> getIngresosByDeptoBetweenDate(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        List<Object[]> resultados = ingresoRepository.findTotalIngresosByDepartamentoBetweenDates(fechaInicio,
                fechaFin);

        List<ListDepartamentosDto> deptos = apiService.getDepartamentos();

        return resultados.stream()
                .map(obj -> {
                    IngresoByDeptosDates dto = new IngresoByDeptosDates();
                    dto.setId((Long) obj[0]);
                    dto.setTotalIngresos(((Number) obj[1]).intValue());
                    dto.setFechaIngreso((String) obj[2]);

                    // Verifica que la lista no sea null
                    if (deptos != null) {
                        deptos.stream()
                                .filter(d -> d.getId().equals(dto.getId()))
                                .findFirst()
                                .ifPresent(d -> dto.setNombreDepartamento(d.getNombreDepartamento()));
                    }

                    return dto;
                })
                .toList();

    }

    public List<IngresosByHorasDto> getIngresosDayByHour() {

        List<Object[]> response = ingresoRepository.findIngresosByHour();

        return response.stream().map(res -> {

            IngresosByHorasDto dto = new IngresosByHorasDto();

            dto.setHora(((Number) res[0]).intValue());
            dto.setTotal(((Number) res[1]).intValue());
            dto.setFecha((String) res[2]);

            return dto;

        }).toList();

    }

}
