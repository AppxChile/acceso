package com.acceso.acceso.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.acceso.acceso.dto.PersonaResponse;
import com.acceso.acceso.dto.SalidasByFechasDto;
import com.acceso.acceso.entities.Ingreso;
import com.acceso.acceso.entities.Salida;
import com.acceso.acceso.exceptions.MyExceptions;
import com.acceso.acceso.repositories.IngresoRepository;
import com.acceso.acceso.repositories.SalidaRepository;
import com.acceso.acceso.services.interfaces.ApiPersonaService;
import com.acceso.acceso.services.interfaces.SalidaService;

@Service
public class SalidaServiceImpl implements SalidaService {

    private final SalidaRepository salidaRepository;


    private final ApiPersonaService apiPersonaService;

    private final IngresoRepository ingresoRepository;

    public SalidaServiceImpl(SalidaRepository salidaRepository, 
            ApiPersonaService apiPersonaService,
            IngresoRepository ingresoRepository) {
        this.salidaRepository = salidaRepository;
        this.apiPersonaService = apiPersonaService;
        this.ingresoRepository = ingresoRepository;
    }

    @Override
    public Salida createSalida(Integer rut) {

        Ingreso ingreso = ingresoRepository.findTopByRutOrderByHoraIngresoDesc(rut)
                .orElseThrow(() -> new IllegalArgumentException("No existe la persona"));

        Optional<Salida> optSalida = salidaRepository.findByIngreso(ingreso);

        if (optSalida.isPresent()) {
            throw new MyExceptions("El rut ya cuenta con una salida para el ingreso");
        }

        Salida salida = new Salida();
        salida.setIngreso(ingreso);
        salida.setHoraSalida(LocalDateTime.now());
        return salidaRepository.save(salida);
    }

    @Override
    public List<SalidasByFechasDto> getSalidasBetweenDates(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        List<Salida> salidas = salidaRepository.findByHoraSalidaBetween(fechaInicio, fechaFin);

        return salidas.stream()
                .map(sali -> {

                    SalidasByFechasDto dto = new SalidasByFechasDto();

                    dto.setFechaSalida(sali.getHoraSalida());
                    dto.setFechaIngreso(sali.getIngreso().getHoraIngreso());

                    PersonaResponse personaResponse = apiPersonaService
                            .getPersonaInfo(sali.getIngreso().getRut());

                    dto.setRut(personaResponse.getRut().toString().concat("-").concat(personaResponse.getVrut()));

                    dto.setNombre(personaResponse.getNombres().concat(" ")
                            .concat(personaResponse.getPaterno().concat(" ").concat(personaResponse.getMaterno())));

                    return dto;

                }).toList();
    }

    @Override
    public Salida save(Salida salida) {
        return salidaRepository.save(salida);
    }

    @Override
    public Salida findById(Long id) {
        return salidaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No existe el id"));
    }

    @Override
    public Optional<Salida> findByIngreso(Ingreso ingreso) {
        return salidaRepository.findByIngreso(ingreso);
    }

}
