package com.sistema.examenes.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sistema.examenes.entidades.HorarioClase;

public interface HorarioClaseRepository extends JpaRepository<HorarioClase, Long> {
}
