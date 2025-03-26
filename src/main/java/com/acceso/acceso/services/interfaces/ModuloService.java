package com.acceso.acceso.services.interfaces;

import java.util.List;

import com.acceso.acceso.entities.Modulo;

public interface ModuloService {

      List<Modulo> getAll();

      Modulo findById(Long id);

}
