package com.acceso.acceso.services.interfaces;

import com.acceso.acceso.entities.Persona;

public interface PersonaService {

     Persona getOrCreatePersona(Integer rut, String serie);

     Persona findByRut(Integer rut);

}
