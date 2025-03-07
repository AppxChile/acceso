package com.acceso.acceso.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.acceso.acceso.entities.Modulo;
import com.acceso.acceso.repositories.ModuloRepository;

@Service
public class ModuloService {

    private final ModuloRepository moduloRepository;

    public ModuloService(ModuloRepository moduloRepository){
        this.moduloRepository= moduloRepository;
    }


    public List<Modulo> getAll(){

        return moduloRepository.findAll();

    }

}
