package com.sistema.examenes.servicios.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sistema.examenes.entidades.Salon;
import com.sistema.examenes.excepciones.RecursoNoEncontradoException;
import com.sistema.examenes.excepciones.ReglaNegocioException;
import com.sistema.examenes.repositorios.SalonRepository;
import com.sistema.examenes.servicios.SalonService;

@Service
public class SalonServiceImpl implements SalonService {

    private final SalonRepository salonRepository;

    public SalonServiceImpl(SalonRepository salonRepository) {
        this.salonRepository = salonRepository;
    }

    @Override
    public Salon registrarSalon(Salon salon) {
        if (salonRepository.existsByNombre(salon.getNombre())) {
            throw new ReglaNegocioException("Ya existe un salón con el nombre " + salon.getNombre());
        }
        salon.setId(null);
        return salonRepository.save(salon);
    }

    @Override
    public List<Salon> listarSalones() {
        return salonRepository.findAll();
    }

    @Override
    public Salon obtenerSalonPorId(Long id) {
        return salonRepository.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("Salón", id));
    }

    @Override
    public Salon actualizarSalon(Long id, Salon datos) {
        Salon actual = obtenerSalonPorId(id);
        if (!actual.getNombre().equals(datos.getNombre()) && salonRepository.existsByNombre(datos.getNombre())) {
            throw new ReglaNegocioException("Ya existe un salón con el nombre " + datos.getNombre());
        }
        actual.setNombre(datos.getNombre());
        actual.setCapacidad(datos.getCapacidad());
        return salonRepository.save(actual);
    }

    @Override
    public void eliminarSalon(Long id) {
        salonRepository.delete(obtenerSalonPorId(id));
    }
}
