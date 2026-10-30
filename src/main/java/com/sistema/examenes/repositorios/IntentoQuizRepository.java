package com.sistema.examenes.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sistema.examenes.entidades.IntentoQuiz;

public interface IntentoQuizRepository extends JpaRepository<IntentoQuiz, Long> {
    List<IntentoQuiz> findByQuizId(Long quizId);
    List<IntentoQuiz> findByEstudianteId(Long estudianteId);
}
