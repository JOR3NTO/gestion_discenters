package com.sistema.examenes.controladores;

import com.sistema.examenes.entidades.HorarioClase;
import com.sistema.examenes.servicios.HorarioClaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/horarios")
public class HorarioClaseController {
    @Autowired
    private HorarioClaseService horarioClaseService;

    @PostMapping
    public HorarioClase registrar(@RequestBody HorarioClase horario) {
        return horarioClaseService.registrarHorario(horario);
    }

    @GetMapping
    public List<HorarioClase> listar() {
        return horarioClaseService.listarHorarios();
    }

    @GetMapping("/{id}")
    public HorarioClase obtener(@PathVariable Long id) {
        return horarioClaseService.obtenerHorarioPorId(id);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        horarioClaseService.eliminarHorario(id);
    }
}
