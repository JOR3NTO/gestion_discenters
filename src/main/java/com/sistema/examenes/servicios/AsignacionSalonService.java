package com.sistema.examenes.servicios;

import java.util.List;

import com.sistema.examenes.dto.AsignacionRequest;
import com.sistema.examenes.entidades.AsignacionSalon;

public interface AsignacionSalonService {
    AsignacionSalon registrarAsignacion(AsignacionRequest request);
    List<AsignacionSalon> listarAsignaciones();
    List<AsignacionSalon> listarPorSalon(Long salonId);
    List<AsignacionSalon> listarPorEstudiante(Long estudianteId);
    AsignacionSalon obtenerAsignacionPorId(Long id);
    void eliminarAsignacion(Long id);
    AsignacionSalon inscribirEstudiante(Long asignacionId, Long estudianteId);
    AsignacionSalon retirarEstudiante(Long asignacionId, Long estudianteId);
}
