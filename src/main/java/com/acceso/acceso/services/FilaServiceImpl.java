package com.acceso.acceso.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.acceso.acceso.dto.FilaDto;
import com.acceso.acceso.dto.FilaResponse;
import com.acceso.acceso.dto.PersonaResponse;
import com.acceso.acceso.dto.UsuarioResponse;
import com.acceso.acceso.entities.Estado;
import com.acceso.acceso.entities.Fila;
import com.acceso.acceso.entities.Ingreso;
import com.acceso.acceso.entities.Modulo;
import com.acceso.acceso.repositories.FilaRepository;
import com.acceso.acceso.services.interfaces.ApiPersonaService;
import com.acceso.acceso.services.interfaces.ApiUsuariosService;
import com.acceso.acceso.services.interfaces.EstadoService;
import com.acceso.acceso.services.interfaces.FilaService;
import com.acceso.acceso.services.interfaces.IngresoService;
import com.acceso.acceso.services.interfaces.ModuloService;

@Service
public class FilaServiceImpl implements FilaService {

    private final FilaRepository filaRepository;

    private final ApiPersonaService apiPersonaService;

    private final ApiUsuariosService apiUsuariosService;

    private final EstadoService estadoService;

    private final ModuloService moduloService;

    private final IngresoService ingresoService;

    public FilaServiceImpl(FilaRepository filaRepository, ApiPersonaService apiPersonaService,
            ApiUsuariosService apiUsuariosService,
            EstadoService estadoService,
            ModuloService moduloService,
            IngresoService ingresoService) {
        this.filaRepository = filaRepository;
        this.apiPersonaService = apiPersonaService;
        this.apiUsuariosService = apiUsuariosService;
        this.estadoService = estadoService;
        this.moduloService=moduloService;
        this.ingresoService=ingresoService;
    }

    @Override
    public List<FilaDto> getFilasByDepartamento(Long departamentoId) {
        List<Fila> filas = filaRepository.findFilasByDepartamento(departamentoId);
        return filas.stream().map(this::convertFilaDto).toList();
    }

    @Override
    public FilaDto convertFilaDto(Fila fila) {
        FilaDto dto = new FilaDto();
        dto.setId(fila.getId());
        dto.setHoraToma(fila.getHoraToma());
        dto.setEstado(fila.getEstado().getNombre());
        dto.setIngresoId(fila.getIngreso().getId());
        dto.setModulo(Optional.ofNullable(fila.getIngreso().getModulo())
                .map(Modulo::getNombre)
                .orElse(null));
        dto.setHoraIngreso(fila.getIngreso().getHoraIngreso());
        dto.setIdModulo(Optional.ofNullable(fila.getIngreso().getModulo())
                .map(Modulo::getId)
                .orElse(null));

        PersonaResponse persona = apiPersonaService.getPersonaInfo(fila.getIngreso().getPersona().getRut());

        dto.setNombre(
                persona.getNombres().concat(" ")
                        .concat(persona.getPaterno().concat(" ").concat(persona.getMaterno())));
        return dto;
    }

    @Override
    public FilaResponse assignIngreso(Long id, String login, Long moduloId) {

        Fila fila = filaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Fila " + id + " no existe"));

        Ingreso ingreso = fila.getIngreso();

        Estado estado = estadoService.findByNombre("EN ATENCION");

        UsuarioResponse usuarioResponse = apiUsuariosService.getUsuario(login);
        if (usuarioResponse == null) {
            throw new IllegalArgumentException("Usuario con login " + login + " no existe");
        }

        Modulo modulo = moduloService.findById(moduloId);
                

        ingreso.setAsignadoA(login);
        ingreso.setModulo(modulo);
        ingresoService.save(ingreso);

        fila.setEstado(estado);
        fila.setHoraToma(LocalDateTime.now());
        filaRepository.save(fila);

        return new FilaResponse(fila.getId(), ingreso.getAsignadoA(), fila.getEstado().getNombre());
    }

    @Override
    public void unassignIngreso(Long id) {
        Fila fila = filaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Fila con ID " + id + " no encontrada"));

        Estado estado = estadoService.findByNombre("DESASIGNADO");

        fila.setEstado(estado);
        fila.setHoraToma(null);
        filaRepository.save(fila);
    }

    @Override
    public FilaResponse finishIngreso(Long id) {
        Fila fila = filaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Fila con ID " + id + " no encontrada"));

        Ingreso ingreso = fila.getIngreso();

        Estado estado = estadoService.findByNombre("FINALIZADO");

        fila.setEstado(estado);
        fila.setHoraFinalizacion(LocalDateTime.now());
        filaRepository.save(fila);

        return new FilaResponse(fila.getId(), ingreso.getAsignadoA(), fila.getEstado().getNombre());
    }

    @Override
    public Fila save(Fila fila) {
        return filaRepository.save(fila);
    }

}
