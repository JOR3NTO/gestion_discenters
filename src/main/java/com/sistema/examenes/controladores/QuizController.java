package com.sistema.examenes.controladores;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.sistema.examenes.dto.RespuestasQuizRequest;
import com.sistema.examenes.dto.ResultadoQuiz;
import com.sistema.examenes.entidades.IntentoQuiz;
import com.sistema.examenes.entidades.Quiz;
import com.sistema.examenes.servicios.QuizService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/quizzes")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Quiz registrar(@Valid @RequestBody Quiz quiz) {
        return quizService.registrarQuiz(quiz);
    }

    @GetMapping
    public List<Quiz> listar() {
        return quizService.listarQuizzes();
    }

    @GetMapping("/{id}")
    public Quiz obtener(@PathVariable Long id) {
        return quizService.obtenerQuizPorId(id);
    }

    @PutMapping("/{id}")
    public Quiz actualizar(@PathVariable Long id, @Valid @RequestBody Quiz quiz) {
        return quizService.actualizarQuiz(id, quiz);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        quizService.eliminarQuiz(id);
    }

    @PostMapping("/{id}/responder")
    public ResultadoQuiz responder(@PathVariable Long id, @Valid @RequestBody RespuestasQuizRequest request) {
        return quizService.responderQuiz(id, request);
    }

    @GetMapping("/{id}/intentos")
    public List<IntentoQuiz> intentosDelQuiz(@PathVariable Long id) {
        return quizService.listarIntentosPorQuiz(id);
    }

    @GetMapping("/intentos/estudiante/{estudianteId}")
    public List<IntentoQuiz> intentosDelEstudiante(@PathVariable Long estudianteId) {
        return quizService.listarIntentosPorEstudiante(estudianteId);
    }
}
