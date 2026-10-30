package com.sistema.examenes.controladores;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.sistema.examenes.dto.AsignacionRequest;
import com.sistema.examenes.entidades.AsignacionSalon;
import com.sistema.examenes.servicios.AsignacionSalonService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/asignaciones")
public class AsignacionSalonController {

    private final AsignacionSalonService asignacionService;

    public AsignacionSalonController(AsignacionSalonService asignacionService) {
        this.asignacionService = asignacionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AsignacionSalon registrar(@Valid @RequestBody AsignacionRequest request) {
        return asignacionService.registrarAsignacion(request);
    }

    @GetMapping
    public List<AsignacionSalon> listar() {
        return asignacionService.listarAsignaciones();
    }

    @GetMapping("/{id}")
    public AsignacionSalon obtener(@PathVariable Long id) {
        return asignacionService.obtenerAsignacionPorId(id);
    }

    @GetMapping("/salon/{salonId}")
    public List<AsignacionSalon> listarPorSalon(@PathVariable Long salonId) {
        return asignacionService.listarPorSalon(salonId);
    }

    @GetMapping("/estudiante/{estudianteId}")
    public List<AsignacionSalon> listarPorEstudiante(@PathVariable Long estudianteId) {
        return asignacionService.listarPorEstudiante(estudianteId);
    }

    @PostMapping("/{id}/estudiantes/{estudianteId}")
    public AsignacionSalon inscribir(@PathVariable Long id, @PathVariable Long estudianteId) {
        return asignacionService.inscribirEstudiante(id, estudianteId);
    }

    @DeleteMapping("/{id}/estudiantes/{estudianteId}")
    public AsignacionSalon retirar(@PathVariable Long id, @PathVariable Long estudianteId) {
        return asignacionService.retirarEstudiante(id, estudianteId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        asignacionService.eliminarAsignacion(id);
    }
}
