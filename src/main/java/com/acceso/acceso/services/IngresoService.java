package com.acceso.acceso.services;

import org.springframework.stereotype.Service;

import com.acceso.acceso.entities.Ingreso;
import com.acceso.acceso.repositories.IngresoRepository;

@Service
public class IngresoService {


    private final IngresoRepository ingresoRepository;

    public IngresoService(IngresoRepository ingresoRepository) {
        this.ingresoRepository = ingresoRepository;
    }


    public Ingreso crearIngreso(Ingreso ingreso){
        return ingresoRepository.save(ingreso);
    }


}
