package com.sistema.examenes.servicios;

import com.sistema.examenes.entidades.Salon;
import java.util.List;

public interface SalonService {
    Salon registrarSalon(Salon salon);
    List<Salon> listarSalones();
    Salon obtenerSalonPorId(Long id);
    void eliminarSalon(Long id);
}
