package com.sistema.examenes.servicios.impl;

import com.sistema.examenes.entidades.HorarioClase;
import com.sistema.examenes.repositorios.HorarioClaseRepository;
import com.sistema.examenes.servicios.HorarioClaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HorarioClaseServiceImpl implements HorarioClaseService {
    @Autowired
    private HorarioClaseRepository horarioClaseRepository;

    @Override
    public HorarioClase registrarHorario(HorarioClase horario) {
        return horarioClaseRepository.save(horario);
    }

    @Override
    public List<HorarioClase> listarHorarios() {
        return horarioClaseRepository.findAll();
    }

    @Override
    public HorarioClase obtenerHorarioPorId(Long id) {
        Optional<HorarioClase> horario = horarioClaseRepository.findById(id);
        return horario.orElse(null);
    }

    @Override
    public void eliminarHorario(Long id) {
        horarioClaseRepository.deleteById(id);
    }
}
