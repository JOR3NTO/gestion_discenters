package com.sistema.examenes.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sistema.examenes.entidades.Salon;

public interface SalonRepository extends JpaRepository<Salon, Long> {
    boolean existsByNombre(String nombre);
}
