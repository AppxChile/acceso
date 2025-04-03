package com.acceso.acceso.services;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.acceso.acceso.dto.DepartamentoResponse;
import com.acceso.acceso.dto.ListDepartamentosDto;
import com.acceso.acceso.dto.UbicacionDto;
import com.acceso.acceso.dto.UbicacionResponse;
import com.acceso.acceso.entities.UbicacionDepartamento;
import com.acceso.acceso.repositories.UbicacionDepartamentoRepository;
import com.acceso.acceso.services.interfaces.ApiDepartamentoService;
import com.acceso.acceso.services.interfaces.UbicacionDepartamentoService;

@Service
public class UbicacionDepartamentoServiceImpl implements UbicacionDepartamentoService {

    private final UbicacionDepartamentoRepository ubicacionDepartamentoRepository;

    private final ApiDepartamentoService apiDepartamentoService;

    public UbicacionDepartamentoServiceImpl(UbicacionDepartamentoRepository ubicacionDepartamentoRepository,
            ApiDepartamentoService apiDepartamentoService) {
        this.ubicacionDepartamentoRepository = ubicacionDepartamentoRepository;
        this.apiDepartamentoService = apiDepartamentoService;
    }

    @Override
    public List<UbicacionResponse> findAll() {

        List<UbicacionDepartamento> response = ubicacionDepartamentoRepository.findAll();

        return response.stream().map(r -> new UbicacionResponse(r.getId(), r.getNombreUbicacion())).toList();

    }

    @Override
    public UbicacionDto getUbicaciones(Long id) {

        UbicacionDepartamento ubicacionDepartamento = ubicacionDepartamentoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Ubicacion " + id + "no existe"));

        UbicacionDto ubicacionDto = new UbicacionDto();
        ubicacionDto.setNombreUbicacion(ubicacionDepartamento.getNombreUbicacion());

        Set<ListDepartamentosDto> deptos = ubicacionDepartamento.getDepartamentos()
                .stream()
                .map(u -> {
                    ListDepartamentosDto depto = new ListDepartamentosDto();

                    depto.setId(u.getId());
                    DepartamentoResponse departamentoResponse = apiDepartamentoService.getDepartamentoById(u.getId());
                    depto.setNombreDepartamento(departamentoResponse.getNombreDepartamento());

                    return depto;

                }).collect(Collectors.toSet());
        ubicacionDto.setDepartamentos(deptos);

        return ubicacionDto;

    }

}
