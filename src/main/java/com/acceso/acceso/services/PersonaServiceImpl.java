package com.acceso.acceso.services;

import org.springframework.stereotype.Service;

import com.acceso.acceso.entities.Persona;
import com.acceso.acceso.repositories.PersonaRepository;
import com.acceso.acceso.services.interfaces.PersonaService;

@Service
public class PersonaServiceImpl implements PersonaService {

    private final PersonaRepository personaRepository;

    public PersonaServiceImpl(PersonaRepository personaRepository) {
        this.personaRepository = personaRepository;
    }

    @Override
    public Persona getOrCreatePersona(Integer rut, String serie) {

        return personaRepository.findByRut(rut)
                .orElseGet(() -> {
                    Persona nuevaPersona = new Persona(rut, serie);
                    return personaRepository.save(nuevaPersona);
                });
    }

    @Override
    public Persona findByRut(Integer rut) {
        return personaRepository.findByRut(rut)
                .orElseThrow(() -> new IllegalArgumentException("No existe el rut " + rut));
    }

}
