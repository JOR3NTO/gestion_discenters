package com.sistema.examenes.repositorios;

import com.sistema.examenes.entidades.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {
}
