package com.sistema.examenes.controladores;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.sistema.examenes.entidades.HorarioClase;
import com.sistema.examenes.servicios.HorarioClaseService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/horarios")
public class HorarioClaseController {

    private final HorarioClaseService horarioService;

    public HorarioClaseController(HorarioClaseService horarioService) {
        this.horarioService = horarioService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HorarioClase registrar(@Valid @RequestBody HorarioClase horario) {
        return horarioService.registrarHorario(horario);
    }

    @GetMapping
    public List<HorarioClase> listar() {
        return horarioService.listarHorarios();
    }

    @GetMapping("/{id}")
    public HorarioClase obtener(@PathVariable Long id) {
        return horarioService.obtenerHorarioPorId(id);
    }

    @PutMapping("/{id}")
    public HorarioClase actualizar(@PathVariable Long id, @Valid @RequestBody HorarioClase horario) {
        return horarioService.actualizarHorario(id, horario);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        horarioService.eliminarHorario(id);
    }
}
