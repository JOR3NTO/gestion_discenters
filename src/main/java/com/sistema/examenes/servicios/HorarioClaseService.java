package com.sistema.examenes.servicios;

import java.util.List;

import com.sistema.examenes.entidades.HorarioClase;

public interface HorarioClaseService {
    HorarioClase registrarHorario(HorarioClase horario);
    List<HorarioClase> listarHorarios();
    HorarioClase obtenerHorarioPorId(Long id);
    HorarioClase actualizarHorario(Long id, HorarioClase horario);
    void eliminarHorario(Long id);
}
