package com.acceso.acceso.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.acceso.acceso.dto.AssignRequest;
import com.acceso.acceso.dto.FilaResponse;
import com.acceso.acceso.dto.FinishRequest;
import com.acceso.acceso.dto.UnassigRequest;
import com.acceso.acceso.dto.UsuarioResponse;
import com.acceso.acceso.entities.Departamento;
import com.acceso.acceso.entities.Estado;
import com.acceso.acceso.entities.Fila;
import com.acceso.acceso.entities.FilaDepartamento;
import com.acceso.acceso.entities.Ingreso;
import com.acceso.acceso.entities.IngresoDepartamento;
import com.acceso.acceso.entities.Modulo;
import com.acceso.acceso.repositories.DepartamentoRepository;
import com.acceso.acceso.repositories.FilaDepartamentoRepository;
import com.acceso.acceso.repositories.FilaRepository;
import com.acceso.acceso.repositories.IngresoDepartamentoRepository;
import com.acceso.acceso.services.interfaces.ApiUsuariosService;
import com.acceso.acceso.services.interfaces.EstadoService;
import com.acceso.acceso.services.interfaces.FilaService;
import com.acceso.acceso.services.interfaces.ModuloService;

@Service
public class FilaServiceImpl implements FilaService {

        private final FilaRepository filaRepository;

        private final ApiUsuariosService apiUsuariosService;

        private final EstadoService estadoService;

        private final ModuloService moduloService;

        private final FilaDepartamentoRepository filaDepartamentoRepository;

        private final DepartamentoRepository departamentoRepository;

        private final IngresoDepartamentoRepository ingresoDepartamentoRepository;

        public FilaServiceImpl(FilaRepository filaRepository,
                        ApiUsuariosService apiUsuariosService,
                        EstadoService estadoService,
                        ModuloService moduloService,
                        IngresoDepartamentoRepository ingresoDepartamentoRepository,
                        FilaDepartamentoRepository filaDepartamentoRepository,
                        DepartamentoRepository departamentoRepository) {
                this.filaRepository = filaRepository;
                this.apiUsuariosService = apiUsuariosService;
                this.estadoService = estadoService;
                this.moduloService = moduloService;
                this.filaDepartamentoRepository = filaDepartamentoRepository;
                this.departamentoRepository = departamentoRepository;
                this.ingresoDepartamentoRepository = ingresoDepartamentoRepository;
        }

        @Override
        public FilaResponse assignIngreso(AssignRequest request) {

                UsuarioResponse usuarioResponse = apiUsuariosService.getUsuario(request.getLogin());
                if (usuarioResponse == null) {
                        throw new IllegalArgumentException("Usuario con login " + request.getLogin() + " no existe");
                }

                Fila fila = filaRepository.findById(request.getId())
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Fila " + request.getId() + " no existe"));

                Departamento departamento = departamentoRepository.findById(usuarioResponse.getIdDepartamento())
                                .orElseThrow(() -> new IllegalArgumentException("No existe departamento"));

                List<FilaDepartamento> listFilaDepartamento = filaDepartamentoRepository.findByFila(fila);

                FilaDepartamento filaDepartamento = listFilaDepartamento.stream()
                                .filter(f -> f.getDepartamento().getId().equals(usuarioResponse.getIdDepartamento()))
                                .findFirst()
                                .orElseThrow(() -> new IllegalArgumentException("No existe departamento"));

                Estado estado = estadoService.findByNombre("EN ATENCION");

                Modulo modulo = moduloService.findById(request.getModuloId());

                Ingreso ingreso = fila.getIngreso();

                IngresoDepartamento ingresoDepartamento = new IngresoDepartamento();

                ingresoDepartamento.setDepartamento(departamento);
                ingresoDepartamento.setIngreso(ingreso);

                filaDepartamento.setEstado(estado);
                filaDepartamento.setFila(fila);
                filaDepartamento.setDepartamento(departamento);
                filaDepartamento.setModulo(modulo);
                filaDepartamento.setAsignadoA(request.getLogin());
                filaDepartamento.setHoraToma(LocalDateTime.now());

                filaDepartamentoRepository.save(filaDepartamento);

                ingresoDepartamentoRepository.save(ingresoDepartamento);

                return new FilaResponse(fila.getId(), filaDepartamento.getAsignadoA(),
                                filaDepartamento.getEstado().getNombre());
        }

        @Override
        public void unassignIngreso(UnassigRequest request) {
                Fila fila = filaRepository.findById(request.getId())
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Fila con ID " + request.getId() + " no encontrada"));

                UsuarioResponse usuarioResponse = apiUsuariosService.getUsuario(request.getLogin());
                if (usuarioResponse == null) {
                        throw new IllegalArgumentException("Usuario con login " + request.getLogin() + " no existe");
                }

                Estado estado = estadoService.findByNombre("DESASIGNADO");

                List<FilaDepartamento> listFilaDepartamento = filaDepartamentoRepository.findByFila(fila);

                FilaDepartamento filaDepartamento = listFilaDepartamento.stream()
                                .filter(f -> f.getDepartamento().getId().equals(usuarioResponse.getIdDepartamento())
                                                && f.getFila().getId().equals(fila.getId()))
                                .findFirst()
                                .orElseThrow(() -> new IllegalArgumentException("No existe la fila"));

                filaDepartamento.setEstado(estado);
                filaDepartamento.setAsignadoA(null);
                filaDepartamento.setModulo(null);
                filaDepartamento.setHoraToma(null);
                filaDepartamentoRepository.save(filaDepartamento);

        }

        @Override
        public FilaResponse finishIngreso(FinishRequest request) {
                Fila fila = filaRepository.findById(request.getId())
                                .orElseThrow(() -> new IllegalArgumentException(
                                                "Fila con ID " + request.getId() + " no encontrada"));

                UsuarioResponse usuarioResponse = apiUsuariosService.getUsuario(request.getLogin());
                if (usuarioResponse == null) {
                        throw new IllegalArgumentException("Usuario con login " + request.getLogin() + " no existe");
                }

                List<FilaDepartamento> listFilaDepartamento = filaDepartamentoRepository.findByFila(fila);

                FilaDepartamento filaDepartamento = listFilaDepartamento.stream()
                                .filter(f -> f.getDepartamento().getId().equals(usuarioResponse.getIdDepartamento())
                                                && f.getFila().getId().equals(fila.getId()))
                                .findFirst()
                                .orElseThrow(() -> new IllegalArgumentException("No existe la fila"));

                Estado estado = estadoService.findByNombre("FINALIZADO");

                filaDepartamento.setEstado(estado);
                filaDepartamento.setHoraFinalizacion(LocalDateTime.now());
                filaDepartamentoRepository.save(filaDepartamento);

                return new FilaResponse(fila.getId(), filaDepartamento.getAsignadoA(),
                                filaDepartamento.getEstado().getNombre());
        }

        @Override
        public Fila save(Fila fila) {
                return filaRepository.save(fila);
        }

}
