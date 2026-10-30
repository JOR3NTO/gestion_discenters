package com.sistema.examenes.servicios.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sistema.examenes.dto.RespuestasQuizRequest;
import com.sistema.examenes.dto.ResultadoQuiz;
import com.sistema.examenes.entidades.IntentoQuiz;
import com.sistema.examenes.entidades.Opcion;
import com.sistema.examenes.entidades.Pregunta;
import com.sistema.examenes.entidades.Quiz;
import com.sistema.examenes.excepciones.RecursoNoEncontradoException;
import com.sistema.examenes.excepciones.ReglaNegocioException;
import com.sistema.examenes.repositorios.IntentoQuizRepository;
import com.sistema.examenes.repositorios.QuizRepository;
import com.sistema.examenes.servicios.EstudianteService;
import com.sistema.examenes.servicios.QuizService;

/**
 * Gestión de quizzes con sus preguntas y opciones, y calificación automática.
 * El puntaje va de 0 a 5 (correctas / total * 5).
 */
@Service
@Transactional
public class QuizServiceImpl implements QuizService {

    private static final double PUNTAJE_MAXIMO = 5.0;

    private final QuizRepository quizRepository;
    private final IntentoQuizRepository intentoRepository;
    private final EstudianteService estudianteService;

    public QuizServiceImpl(QuizRepository quizRepository, IntentoQuizRepository intentoRepository,
            EstudianteService estudianteService) {
        this.quizRepository = quizRepository;
        this.intentoRepository = intentoRepository;
        this.estudianteService = estudianteService;
    }

    @Override
    public Quiz registrarQuiz(Quiz quiz) {
        quiz.setId(null);
        validarPreguntas(quiz);
        enlazar(quiz);
        return quizRepository.save(quiz);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Quiz> listarQuizzes() {
        return quizRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Quiz obtenerQuizPorId(Long id) {
        return quizRepository.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("Quiz", id));
    }

    @Override
    public Quiz actualizarQuiz(Long id, Quiz datos) {
        Quiz actual = obtenerQuizPorId(id);
        validarPreguntas(datos);
        actual.setTitulo(datos.getTitulo());
        actual.setDescripcion(datos.getDescripcion());
        actual.setActivo(datos.isActivo());
        actual.getPreguntas().clear();
        actual.getPreguntas().addAll(datos.getPreguntas());
        enlazar(actual);
        return quizRepository.save(actual);
    }

    @Override
    public void eliminarQuiz(Long id) {
        if (!intentoRepository.findByQuizId(id).isEmpty()) {
            throw new ReglaNegocioException("No se puede eliminar un quiz que ya tiene intentos registrados");
        }
        quizRepository.delete(obtenerQuizPorId(id));
    }

    @Override
    public ResultadoQuiz responderQuiz(Long quizId, RespuestasQuizRequest request) {
        Quiz quiz = obtenerQuizPorId(quizId);
        if (!quiz.isActivo()) {
            throw new ReglaNegocioException("El quiz no está activo");
        }

        int correctas = 0;
        Map<Long, Long> respuestas = request.respuestas();
        for (Pregunta pregunta : quiz.getPreguntas()) {
            Long opcionId = respuestas.get(pregunta.getId());
            boolean acierto = opcionId != null && pregunta.getOpciones().stream()
                    .anyMatch(o -> o.getId().equals(opcionId) && o.isCorrecta());
            if (acierto) {
                correctas++;
            }
        }
        int total = quiz.getPreguntas().size();

        IntentoQuiz intento = new IntentoQuiz();
        intento.setQuiz(quiz);
        intento.setEstudiante(estudianteService.obtenerEstudiantePorId(request.estudianteId()));
        intento.setCorrectas(correctas);
        intento.setTotal(total);
        intento.setPuntaje(total == 0 ? 0 : Math.round(correctas * PUNTAJE_MAXIMO / total * 100) / 100.0);
        intento = intentoRepository.save(intento);

        return new ResultadoQuiz(intento.getId(), total, correctas, intento.getPuntaje());
    }

    @Override
    @Transactional(readOnly = true)
    public List<IntentoQuiz> listarIntentosPorQuiz(Long quizId) {
        obtenerQuizPorId(quizId);
        return intentoRepository.findByQuizId(quizId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<IntentoQuiz> listarIntentosPorEstudiante(Long estudianteId) {
        estudianteService.obtenerEstudiantePorId(estudianteId);
        return intentoRepository.findByEstudianteId(estudianteId);
    }

    /** Cada pregunta necesita al menos dos opciones y exactamente una correcta. */
    private void validarPreguntas(Quiz quiz) {
        for (Pregunta pregunta : quiz.getPreguntas()) {
            long correctas = pregunta.getOpciones().stream().filter(Opcion::isCorrecta).count();
            if (pregunta.getOpciones().size() < 2 || correctas != 1) {
                throw new ReglaNegocioException("La pregunta '" + pregunta.getEnunciado()
                        + "' debe tener al menos dos opciones y exactamente una correcta");
            }
        }
    }

    /** Completa las referencias inversas que JSON no trae. */
    private void enlazar(Quiz quiz) {
        for (Pregunta pregunta : quiz.getPreguntas()) {
            pregunta.setId(null);
            pregunta.setQuiz(quiz);
            for (Opcion opcion : pregunta.getOpciones()) {
                opcion.setId(null);
                opcion.setPregunta(pregunta);
            }
        }
    }
}
