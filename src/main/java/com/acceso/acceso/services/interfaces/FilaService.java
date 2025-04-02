package com.acceso.acceso.services.interfaces;

import com.acceso.acceso.dto.AssignRequest;
import com.acceso.acceso.dto.FilaResponse;
import com.acceso.acceso.dto.FinishRequest;
import com.acceso.acceso.dto.UnassigRequest;
import com.acceso.acceso.entities.Fila;

public interface FilaService {


    FilaResponse assignIngreso(AssignRequest request);

    void unassignIngreso(UnassigRequest request);

    FilaResponse finishIngreso(FinishRequest request);

    Fila save(Fila fila);

}
