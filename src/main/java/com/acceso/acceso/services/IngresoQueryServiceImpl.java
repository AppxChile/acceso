package com.acceso.acceso.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.acceso.acceso.dto.IngresoByDeptosDates;
import com.acceso.acceso.dto.IngresoWithouSalidaDto;
import com.acceso.acceso.dto.IngresosByFechasDto;
import com.acceso.acceso.dto.IngresosByHorasDto;
import com.acceso.acceso.dto.ListDepartamentosDto;
import com.acceso.acceso.dto.PersonaResponse;
import com.acceso.acceso.entities.Ingreso;
import com.acceso.acceso.repositories.IngresoRepository;
import com.acceso.acceso.services.interfaces.ApiDepartamentoService;
import com.acceso.acceso.services.interfaces.ApiPersonaService;
import com.acceso.acceso.services.interfaces.IngresoQueryService;

@Service
public class IngresoQueryServiceImpl implements IngresoQueryService {

    private final IngresoRepository ingresoRepository;

    private final ApiPersonaService apiPersonaService;

    private final ApiDepartamentoService apiDepartamentoService;

    public IngresoQueryServiceImpl(IngresoRepository ingresoRepository, ApiPersonaService apiPersonaService,
            ApiDepartamentoService apiDepartamentoService) {
        this.ingresoRepository = ingresoRepository;
        this.apiPersonaService = apiPersonaService;
        this.apiDepartamentoService = apiDepartamentoService;
    }

    @Override
    public List<IngresosByFechasDto> getIngresosBetweenDates(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        List<Ingreso> ingresos = ingresoRepository.findByhoraIngresoBetween(fechaInicio, fechaFin);

        return ingresos.stream().map(ingr -> {
            IngresosByFechasDto dto = new IngresosByFechasDto();
            dto.setFechaIngreso(ingr.getHoraIngreso());

            if (ingr.getRut() != null) {
                PersonaResponse personaResponse = apiPersonaService.getPersonaInfo(ingr.getRut());
                dto.setNombre(personaResponse.getNombreCompleto());
                dto.setRut(personaResponse.getRut().toString().concat("-").concat(personaResponse.getVrut()));
            } else {
                dto.setNombre("Desconocido");
            }

            if (ingr.getSalida() != null) {
                dto.setFechaSalida(ingr.getHoraSalida());
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

            PersonaResponse personaResponse = apiPersonaService.getPersonaInfo(ing.getRut());

            dto.setRut(personaResponse.getRut().toString().concat("-").concat(personaResponse.getVrut()));
            dto.setNombre(personaResponse.getNombreCompleto());
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

}
