package com.sistema.examenes.repositorios;

import com.sistema.examenes.entidades.Salon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalonRepository extends JpaRepository<Salon, Long> {
}
