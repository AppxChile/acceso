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
import com.acceso.acceso.repositories.EstadoRepository;
import com.acceso.acceso.repositories.FilaRepository;
import com.acceso.acceso.repositories.IngresoRepository;
import com.acceso.acceso.repositories.ModuloRepository;

@Service
public class FilaService {

        private final FilaRepository filaRepository;

        private final ApiService apiService;

        private final EstadoRepository estadoRepository;

        private final IngresoRepository ingresoRepository;

        private final ModuloRepository moduloRepository;

        public FilaService(FilaRepository filaRepository, ApiService apiService, EstadoRepository estadoRepository,
                        IngresoRepository ingresoRepository, ModuloRepository moduloRepository) {
                this.filaRepository = filaRepository;
                this.apiService = apiService;
                this.estadoRepository = estadoRepository;
                this.ingresoRepository = ingresoRepository;
                this.moduloRepository = moduloRepository;
        }

        public List<FilaDto> getFilasByDepartamento(Long departamentoId) {
                List<Fila> filas = filaRepository.findFilasByDepartamento(departamentoId);
                return filas.stream().map(this::convertFilaDto).toList();
        }

        private FilaDto convertFilaDto(Fila fila) {
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

                PersonaResponse persona = apiService.getPersonaInfo(fila.getIngreso().getPersona().getRut());

                dto.setNombre(
                                persona.getNombres().concat(" ")
                                                .concat(persona.getPaterno().concat(" ").concat(persona.getMaterno())));
                return dto;
        }

        public FilaResponse assignIngreso(Long id, String login, Long moduloId) {

                Fila fila = filaRepository.findById(id)
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Fila con ID " + id + " no encontrada"));

                Ingreso ingreso = fila.getIngreso();

                Estado estado = estadoRepository.findByNombre("EN ATENCION")
                                .orElseThrow(() -> new IllegalArgumentException("Estado 'EN ATENCION' no existe"));

                UsuarioResponse usuarioResponse = apiService.getUsuario(login);
                if (usuarioResponse == null) {
                        throw new IllegalArgumentException("Usuario con login " + login + " no existe");
                }

                Modulo modulo = moduloRepository.findById(moduloId)
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Módulo con ID " + moduloId + " no encontrado"));

                ingreso.setAsignadoA(login);
                ingreso.setModulo(modulo);
                ingresoRepository.save(ingreso);

                fila.setEstado(estado);
                fila.setHoraToma(LocalDateTime.now());
                filaRepository.save(fila);

                return new FilaResponse(fila.getId(), ingreso.getAsignadoA(), fila.getEstado().getNombre());
        }

        public FilaResponse finishIngreso(Long id) {

                Fila fila = filaRepository.findById(id)
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Fila con ID " + id + " no encontrada"));

                Ingreso ingreso = fila.getIngreso();

                Estado estado = estadoRepository.findByNombre("FINALIZADO")
                                .orElseThrow(() -> new IllegalArgumentException("Estado 'FINALIZADO' no existe"));

                fila.setEstado(estado);
                fila.setHoraFinalizacion(LocalDateTime.now());
                filaRepository.save(fila);

                return new FilaResponse(fila.getId(), ingreso.getAsignadoA(), fila.getEstado().getNombre());
        }

}
