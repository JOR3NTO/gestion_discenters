package com.sistema.examenes.servicios.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sistema.examenes.entidades.HorarioClase;
import com.sistema.examenes.excepciones.RecursoNoEncontradoException;
import com.sistema.examenes.excepciones.ReglaNegocioException;
import com.sistema.examenes.repositorios.HorarioClaseRepository;
import com.sistema.examenes.servicios.HorarioClaseService;

@Service
public class HorarioClaseServiceImpl implements HorarioClaseService {

    private final HorarioClaseRepository horarioRepository;

    public HorarioClaseServiceImpl(HorarioClaseRepository horarioRepository) {
        this.horarioRepository = horarioRepository;
    }

    @Override
    public HorarioClase registrarHorario(HorarioClase horario) {
        validarRango(horario);
        horario.setId(null);
        return horarioRepository.save(horario);
    }

    @Override
    public List<HorarioClase> listarHorarios() {
        return horarioRepository.findAll();
    }

    @Override
    public HorarioClase obtenerHorarioPorId(Long id) {
        return horarioRepository.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("Horario", id));
    }

    @Override
    public HorarioClase actualizarHorario(Long id, HorarioClase datos) {
        validarRango(datos);
        HorarioClase actual = obtenerHorarioPorId(id);
        actual.setDiaSemana(datos.getDiaSemana());
        actual.setHoraInicio(datos.getHoraInicio());
        actual.setHoraFin(datos.getHoraFin());
        return horarioRepository.save(actual);
    }

    @Override
    public void eliminarHorario(Long id) {
        horarioRepository.delete(obtenerHorarioPorId(id));
    }

    private void validarRango(HorarioClase horario) {
        if (!horario.getHoraInicio().isBefore(horario.getHoraFin())) {
            throw new ReglaNegocioException("La hora de inicio debe ser anterior a la hora de fin");
        }
    }
}
