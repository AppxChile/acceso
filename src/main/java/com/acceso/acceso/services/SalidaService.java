package com.acceso.acceso.services;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.acceso.acceso.entities.Ingreso;
import com.acceso.acceso.entities.Persona;
import com.acceso.acceso.entities.Salida;
import com.acceso.acceso.exceptions.MyExceptions;
import com.acceso.acceso.repositories.IngresoRepository;
import com.acceso.acceso.repositories.PersonaRepository;
import com.acceso.acceso.repositories.SalidaRepository;

@Service
public class SalidaService {


    private final IngresoRepository ingresoRepository;

    private final SalidaRepository salidaRepository;

    private final PersonaRepository personaRepository;

    public SalidaService( IngresoRepository ingresoRepository,
            SalidaRepository salidaRepository,
            PersonaRepository personaRepository) {
        this.ingresoRepository = ingresoRepository;
        this.salidaRepository = salidaRepository;
        this.personaRepository= personaRepository;
    }

    public Salida createSalida(Integer rut) {

        Persona persona = personaRepository.findByRut(rut).orElseThrow(()->new MyExceptions("No Existe el rut"));

        Optional<Ingreso> optIngreso = ingresoRepository.findTopByPersonaOrderByHoraIngresoDesc(persona);

        if(!optIngreso.isPresent()){
            throw new MyExceptions("No existe ingereso para el rut dado");
        }

        Ingreso ingreso = optIngreso.get();

        Optional<Salida> optSalida = salidaRepository.findByIngreso(ingreso);

        if(optSalida.isPresent()){
            throw new MyExceptions("El rut ya cuenta con una salida para el ingreso");
        }

        Salida salida = new Salida();
        salida.setIngreso(ingreso);
        salida.setHoraSalida(LocalDateTime.now());
        return salidaRepository.save(salida);


        






      
       

    }

}
