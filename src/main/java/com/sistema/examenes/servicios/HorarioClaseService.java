package com.sistema.examenes.servicios;

import com.sistema.examenes.entidades.HorarioClase;
import java.util.List;

public interface HorarioClaseService {
    HorarioClase registrarHorario(HorarioClase horario);
    List<HorarioClase> listarHorarios();
    HorarioClase obtenerHorarioPorId(Long id);
    void eliminarHorario(Long id);
}
