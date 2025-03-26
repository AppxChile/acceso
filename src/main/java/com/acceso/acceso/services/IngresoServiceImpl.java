package com.acceso.acceso.services;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.acceso.acceso.dto.DepartamentoResponse;
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
import com.acceso.acceso.repositories.IngresoRepository;
import com.acceso.acceso.services.interfaces.ApiDepartamentoService;
import com.acceso.acceso.services.interfaces.ApiPersonaService;
import com.acceso.acceso.services.interfaces.DepartamentoService;
import com.acceso.acceso.services.interfaces.EstadoService;
import com.acceso.acceso.services.interfaces.FilaService;
import com.acceso.acceso.services.interfaces.IngresoDepartamentoService;
import com.acceso.acceso.services.interfaces.IngresoService;
import com.acceso.acceso.services.interfaces.PersonaService;
import com.acceso.acceso.services.interfaces.SalidaService;

@Service
public class IngresoServiceImpl implements IngresoService {

    private final PersonaService personaService;

    private final DepartamentoService departamentoService;

    private final EstadoService estadoService;

    private final ApiPersonaService apiPersonaService;

    private final ApiDepartamentoService apiDepartamentoService;

    private final FilaService filaService;

    private final IngresoDepartamentoService ingresoDepartamentoService;

    private final SalidaService salidaService;

    private final IngresoRepository ingresoRepository;

    public IngresoServiceImpl(PersonaService personaService, DepartamentoService departamentoService,
            EstadoService estadoService,
            IngresoRepository ingresoRepository,
            ApiPersonaService apiPersonaService,
            IngresoDepartamentoService ingresoDepartamentoService,
            ApiDepartamentoService apiDepartamentoService,
            FilaService filaService,
            SalidaService salidaService) {
        this.personaService = personaService;
        this.departamentoService = departamentoService;
        this.estadoService = estadoService;
        this.ingresoRepository = ingresoRepository;
        this.apiPersonaService = apiPersonaService;
        this.ingresoDepartamentoService = ingresoDepartamentoService;
        this.apiDepartamentoService = apiDepartamentoService;
        this.filaService = filaService;
        this.salidaService=salidaService;
    }

    @Override
    public IngresoDto createIngreso(IngresoRequest request) {

        Persona persona = personaService.getOrCreatePersona(request.getRut(), request.getSerie());

        if (hasIngresoWithoutSalida(persona)) {
            throw new MyExceptions("Persona no tiene registrada una salida");
        }

        Set<DepartamentoResponse> departamentos = request.getIdDepartamentos().stream()
                .map(departamentoService::findById)
                .collect(Collectors.toSet());

        Ingreso ingreso = new Ingreso();

        ingreso.setHoraIngreso(fechaHoraIngreso());
        ingreso.setPersona(persona);

        ingreso = ingresoRepository.save(ingreso);

        for (DepartamentoResponse departamento : departamentos) {
            IngresoDepartamento ingresoDepartamento = new IngresoDepartamento();
            Departamento depto = new Departamento();
            depto.setId(departamento.getId());
            ingresoDepartamento.setIngreso(ingreso);
            ingresoDepartamento.setDepartamento(depto);
            ingresoDepartamentoService.save(ingresoDepartamento);
        }

        Estado estadoInicial = estadoService.findByNombre("EN ESPERA");

        Fila fila = new Fila();

        fila.setIngreso(ingreso);
        fila.setEstado(estadoInicial);
        filaService.save(fila);

        return convertDTO(ingreso, estadoInicial);
    }

    @Override
    public List<IngresosByFechasDto> getIngresosBetweenDates(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        List<Ingreso> ingresos = ingresoRepository.findByhoraIngresoBetween(fechaInicio, fechaFin);

        return ingresos.stream().map(ingr -> {
            IngresosByFechasDto dto = new IngresosByFechasDto();
            dto.setFechaIngreso(ingr.getHoraIngreso());

            if (ingr.getPersona() != null) {
                PersonaResponse personaResponse = apiPersonaService.getPersonaInfo(ingr.getPersona().getRut());
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

    @Override
    public List<IngresoWithouSalidaDto> getIngresoSalidaNull() {

        List<Ingreso> ingresos = ingresoRepository.findBySalidaIsNull();

        return ingresos.stream().map(ing -> {

            IngresoWithouSalidaDto dto = new IngresoWithouSalidaDto();

            PersonaResponse personaResponse = apiPersonaService.getPersonaInfo(ing.getPersona().getRut());

            dto.setRut(personaResponse.getRut().toString().concat("-").concat(personaResponse.getVrut()));
            dto.setNombre(personaResponse.getNombres().concat(" ")
                    .concat(personaResponse.getPaterno().concat(" ").concat(personaResponse.getMaterno())));
            dto.setHoraIngreso(ing.getHoraIngreso());

            return dto;

        }).toList();

    }

    @Override
    public List<IngresoByDeptosDates> getIngresosByDeptoBetweenDate(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        List<Object[]> resultados = ingresoRepository.findTotalIngresosByDepartamentoBetweenDates(fechaInicio,
                fechaFin);

        List<ListDepartamentosDto> deptos = apiDepartamentoService.getDepartamentos();

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

    @Override
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

    private boolean hasIngresoWithoutSalida(Persona persona) {
        Optional<Ingreso> optUltimoIngreso = ingresoRepository.findTopByPersonaOrderByHoraIngresoDesc(persona);

        if (optUltimoIngreso.isEmpty()) {
            return false;
        }

        Ingreso ultimoIngreso = optUltimoIngreso.get();

        Optional<Salida> optSalida = salidaService.findByIngreso(ultimoIngreso);

        return optSalida.isEmpty();
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

    @Override
    public Ingreso save(Ingreso ingreso) {
        return ingresoRepository.save(ingreso);
    }

    @Override
    public Ingreso findTopByPersonaOrderByHoraIngresoDesc(Persona persona) {
        return ingresoRepository.findTopByPersonaOrderByHoraIngresoDesc(persona)
                .orElseThrow(() -> new MyExceptions("no existe ingreso para el rut"));
    }
}
