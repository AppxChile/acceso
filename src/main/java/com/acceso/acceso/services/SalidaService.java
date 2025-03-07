package com.acceso.acceso.services;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.acceso.acceso.entities.Ingreso;
import com.acceso.acceso.entities.Salida;
import com.acceso.acceso.exceptions.MyExceptions;
import com.acceso.acceso.repositories.IngresoRepository;
import com.acceso.acceso.repositories.SalidaRepository;

@Service
public class SalidaService {


    private final IngresoRepository ingresoRepository;

    private final SalidaRepository salidaRepository;

    public SalidaService( IngresoRepository ingresoRepository,
            SalidaRepository salidaRepository) {
        this.ingresoRepository = ingresoRepository;
        this.salidaRepository = salidaRepository;
    }

    public Salida createSalida(Long id) {

      

        Ingreso ingreso = ingresoRepository.findById(id)
                          .orElseThrow(()-> new IllegalArgumentException("Id no encontrado"));

        if(salidaRepository.findByIngresoId(id).isPresent()){
            throw new MyExceptions("El id ya tiene una salida");
        }

        Salida salida = new Salida();
        salida.setIngreso(ingreso);
        salida.setHoraSalida(LocalDateTime.now());
        return salidaRepository.save(salida);

    }

}
