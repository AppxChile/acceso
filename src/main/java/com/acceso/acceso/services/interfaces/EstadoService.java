package com.acceso.acceso.services.interfaces;

import com.acceso.acceso.entities.Estado;

public interface EstadoService {

    Estado createEstado(Estado estadoRequest) ;

    Estado findByNombre(String name);

}
