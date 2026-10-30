package com.sistema.examenes.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sistema.examenes.entidades.Quiz;

public interface QuizRepository extends JpaRepository<Quiz, Long> {
}
