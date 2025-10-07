package com.sistema.examenes.controladores;

import com.sistema.examenes.entidades.Estudiante;
import com.sistema.examenes.servicios.EstudianteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteController {
    @Autowired
    private EstudianteService estudianteService;

    @PostMapping
    public Estudiante registrar(@RequestBody Estudiante estudiante) {
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

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        estudianteService.eliminarEstudiante(id);
    }
}
