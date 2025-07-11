package com.acceso.acceso.services;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.acceso.acceso.dto.DepartamentoResponse;
import com.acceso.acceso.dto.IngresoDto;
import com.acceso.acceso.dto.IngresoRequest;
import com.acceso.acceso.dto.PersonaDto;
import com.acceso.acceso.entities.Departamento;
import com.acceso.acceso.entities.Estado;
import com.acceso.acceso.entities.Fila;
import com.acceso.acceso.entities.FilaDepartamento;
import com.acceso.acceso.entities.Ingreso;
import com.acceso.acceso.entities.IngresoDepartamento;
import com.acceso.acceso.entities.IngresoUbicacion;
import com.acceso.acceso.entities.Salida;
import com.acceso.acceso.entities.UbicacionDepartamento;
import com.acceso.acceso.exceptions.MyExceptions;
import com.acceso.acceso.repositories.FilaDepartamentoRepository;
import com.acceso.acceso.repositories.IngresoRepository;
import com.acceso.acceso.repositories.IngresoUbicacionRepository;
import com.acceso.acceso.repositories.UbicacionDepartamentoRepository;
import com.acceso.acceso.services.interfaces.DepartamentoService;
import com.acceso.acceso.services.interfaces.EstadoService;
import com.acceso.acceso.services.interfaces.FilaService;
import com.acceso.acceso.services.interfaces.IngresoService;
import com.acceso.acceso.services.interfaces.SalidaService;

@Service
public class IngresoServiceImpl implements IngresoService {

    private final DepartamentoService departamentoService;

    private final EstadoService estadoService;

    private final FilaService filaService;

    private final SalidaService salidaService;

    private final IngresoRepository ingresoRepository;

    private final UbicacionDepartamentoRepository ubicacionDepartamentoRepository;

    private final IngresoUbicacionRepository ingresoUbicacionRepository;

    private final FilaDepartamentoRepository filaDepartamentoRepository;

    public IngresoServiceImpl(DepartamentoService departamentoService,
            EstadoService estadoService,
            IngresoRepository ingresoRepository,
            FilaService filaService,
            SalidaService salidaService,
            UbicacionDepartamentoRepository ubicacionDepartamentoRepository,
            IngresoUbicacionRepository ingresoUbicacionRepository,
            FilaDepartamentoRepository filaDepartamentoRepository) {
        this.departamentoService = departamentoService;
        this.estadoService = estadoService;
        this.ingresoRepository = ingresoRepository;
        this.filaService = filaService;
        this.salidaService = salidaService;
        this.ubicacionDepartamentoRepository = ubicacionDepartamentoRepository;
        this.ingresoUbicacionRepository = ingresoUbicacionRepository;
        this.filaDepartamentoRepository = filaDepartamentoRepository;
    }

    @Override
    public IngresoDto createIngreso(IngresoRequest request) {

        if (hasIngresoWithoutSalida(request.getRut())) {
            throw new MyExceptions("Persona no tiene registrada una salida");
        }

        UbicacionDepartamento ubicacion = ubicacionDepartamentoRepository.findById(request.getIdUbicacion())
                .orElseThrow(() -> new IllegalArgumentException("Ubicacion edificaion inexistente"));

        Set<DepartamentoResponse> departamentos = request.getIdDepartamentos().stream()
                .map(departamentoService::findById)
                .collect(Collectors.toSet());

        Ingreso ingreso = new Ingreso();

        ingreso.setHoraIngreso(fechaHoraIngreso());
        ingreso.setRut(request.getRut());
        ingreso.setSerie(request.getSerie());

        ingreso = ingresoRepository.save(ingreso);

        IngresoUbicacion ingresoUbicacion = new IngresoUbicacion();

        ingresoUbicacion.setIngreso(ingreso);
        ingresoUbicacion.setUbicacionDepartamento(ubicacion);

        ingresoUbicacionRepository.save(ingresoUbicacion);

        Estado estadoInicial = estadoService.findByNombre("EN ESPERA");

        Fila fila = new Fila();

        fila.setIngreso(ingreso);
        fila.setHoraIngresoFila(fechaHoraIngreso());

        fila = filaService.save(fila);

        for (DepartamentoResponse departamento : departamentos) {
            FilaDepartamento filaDepartamento = new FilaDepartamento();
            Departamento depto = new Departamento();
            depto.setId(departamento.getId());
            filaDepartamento.setDepartamento(depto);
            filaDepartamento.setFila(fila);
            filaDepartamento.setEstado(estadoInicial);
            filaDepartamentoRepository.save(filaDepartamento);

        }

        return convertDTO(ingreso, estadoInicial);
    }

    private boolean hasIngresoWithoutSalida(Integer rut) {
        Optional<Ingreso> optUltimoIngreso = ingresoRepository.findTopByRutOrderByHoraIngresoDesc(rut);

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
        personaDTO.setRut(ingreso.getRut());
        personaDTO.setSerie(ingreso.getSerie());
        dto.setPersona(personaDTO);

        List<Long> departamentos = Optional.ofNullable(ingreso.getIngresoDepartamentos())
                .orElse(List.of())
                .stream()
                .map(IngresoDepartamento::getIdDepartamento)
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
    public Ingreso findTopByRutOrderByHoraIngresoDesc(Integer rut) {
        return ingresoRepository.findTopByRutOrderByHoraIngresoDesc(rut)
                .orElseThrow(() -> new MyExceptions("no existe ingreso para el rut"));
    }
}
