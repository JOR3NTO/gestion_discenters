package com.sistema.examenes.controladores;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.sistema.examenes.entidades.Estudiante;
import com.sistema.examenes.servicios.EstudianteService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteController {

    private final EstudianteService estudianteService;

    public EstudianteController(EstudianteService estudianteService) {
        this.estudianteService = estudianteService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Estudiante registrar(@Valid @RequestBody Estudiante estudiante) {
        return estudianteService.registrarEstudiante(estudiante);
    }

    @GetMapping
    public List<Estudiante> listar() {
        return estudianteService.listarEstudiantes();
    }

    @GetMapping("/{id}")
    public Estudiante obtener(@PathVariable Long id) {
        return estudianteService.obtenerEstudiantePorId(id);
    }

    @PutMapping("/{id}")
    public Estudiante actualizar(@PathVariable Long id, @Valid @RequestBody Estudiante estudiante) {
        return estudianteService.actualizarEstudiante(id, estudiante);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        estudianteService.eliminarEstudiante(id);
    }
}
