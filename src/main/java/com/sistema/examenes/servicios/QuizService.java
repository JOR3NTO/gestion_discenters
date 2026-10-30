package com.sistema.examenes.servicios;

import java.util.List;

import com.sistema.examenes.dto.RespuestasQuizRequest;
import com.sistema.examenes.dto.ResultadoQuiz;
import com.sistema.examenes.entidades.IntentoQuiz;
import com.sistema.examenes.entidades.Quiz;

public interface QuizService {
    Quiz registrarQuiz(Quiz quiz);
    List<Quiz> listarQuizzes();
    Quiz obtenerQuizPorId(Long id);
    Quiz actualizarQuiz(Long id, Quiz quiz);
    void eliminarQuiz(Long id);
    ResultadoQuiz responderQuiz(Long quizId, RespuestasQuizRequest respuestas);
    List<IntentoQuiz> listarIntentosPorQuiz(Long quizId);
    List<IntentoQuiz> listarIntentosPorEstudiante(Long estudianteId);
}
