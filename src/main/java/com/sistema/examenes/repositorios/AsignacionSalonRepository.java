package com.sistema.examenes.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sistema.examenes.entidades.AsignacionSalon;

public interface AsignacionSalonRepository extends JpaRepository<AsignacionSalon, Long> {
    List<AsignacionSalon> findBySalonId(Long salonId);
    List<AsignacionSalon> findByEstudiantesId(Long estudianteId);
}
