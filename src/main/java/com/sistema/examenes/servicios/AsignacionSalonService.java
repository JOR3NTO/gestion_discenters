package com.sistema.examenes.servicios;

import com.sistema.examenes.entidades.AsignacionSalon;
import java.util.List;

public interface AsignacionSalonService {
    AsignacionSalon asignarSalon(AsignacionSalon asignacion);
    List<AsignacionSalon> listarAsignaciones();
    AsignacionSalon obtenerAsignacionPorId(Long id);
    void eliminarAsignacion(Long id);
}
