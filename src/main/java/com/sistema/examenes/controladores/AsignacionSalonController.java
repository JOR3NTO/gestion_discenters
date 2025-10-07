package com.sistema.examenes.controladores;

import com.sistema.examenes.entidades.AsignacionSalon;
import com.sistema.examenes.servicios.AsignacionSalonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/asignaciones-salon")
public class AsignacionSalonController {
    @Autowired
    private AsignacionSalonService asignacionSalonService;

    @PostMapping
    public AsignacionSalon asignar(@RequestBody AsignacionSalon asignacion) {
        return asignacionSalonService.asignarSalon(asignacion);
    }

    @GetMapping
    public List<AsignacionSalon> listar() {
        return asignacionSalonService.listarAsignaciones();
    }

    @GetMapping("/{id}")
    public AsignacionSalon obtener(@PathVariable Long id) {
        return asignacionSalonService.obtenerAsignacionPorId(id);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        asignacionSalonService.eliminarAsignacion(id);
    }
}
