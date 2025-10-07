package com.sistema.examenes.servicios.impl;

import com.sistema.examenes.entidades.Salon;
import com.sistema.examenes.repositorios.SalonRepository;
import com.sistema.examenes.servicios.SalonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SalonServiceImpl implements SalonService {
    @Autowired
    private SalonRepository salonRepository;

    @Override
    public Salon registrarSalon(Salon salon) {
        return salonRepository.save(salon);
    }

    @Override
    public List<Salon> listarSalones() {
        return salonRepository.findAll();
    }

    @Override
    public Salon obtenerSalonPorId(Long id) {
        Optional<Salon> salon = salonRepository.findById(id);
        return salon.orElse(null);
    }

    @Override
    public void eliminarSalon(Long id) {
        salonRepository.deleteById(id);
    }
}
