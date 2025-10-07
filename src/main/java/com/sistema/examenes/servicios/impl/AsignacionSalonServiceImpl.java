package com.sistema.examenes.servicios.impl;

import com.sistema.examenes.entidades.AsignacionSalon;
import com.sistema.examenes.repositorios.AsignacionSalonRepository;
import com.sistema.examenes.servicios.AsignacionSalonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AsignacionSalonServiceImpl implements AsignacionSalonService {
    @Autowired
    private AsignacionSalonRepository asignacionSalonRepository;

    @Override
    public AsignacionSalon asignarSalon(AsignacionSalon asignacion) {
        return asignacionSalonRepository.save(asignacion);
    }

    @Override
    public List<AsignacionSalon> listarAsignaciones() {
        return asignacionSalonRepository.findAll();
    }

    @Override
    public AsignacionSalon obtenerAsignacionPorId(Long id) {
        Optional<AsignacionSalon> asignacion = asignacionSalonRepository.findById(id);
        return asignacion.orElse(null);
    }

    @Override
    public void eliminarAsignacion(Long id) {
        asignacionSalonRepository.deleteById(id);
    }
}
