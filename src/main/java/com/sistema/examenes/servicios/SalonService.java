package com.sistema.examenes.servicios;

import java.util.List;

import com.sistema.examenes.entidades.Salon;

public interface SalonService {
    Salon registrarSalon(Salon salon);
    List<Salon> listarSalones();
    Salon obtenerSalonPorId(Long id);
    Salon actualizarSalon(Long id, Salon salon);
    void eliminarSalon(Long id);
}
