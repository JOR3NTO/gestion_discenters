package com.sistema.examenes.controladores;

import com.sistema.examenes.entidades.Salon;
import com.sistema.examenes.servicios.SalonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/salones")
public class SalonController {
    @Autowired
    private SalonService salonService;

    @PostMapping
    public Salon registrar(@RequestBody Salon salon) {
        return salonService.registrarSalon(salon);
    }

    @GetMapping
    public List<Salon> listar() {
        return salonService.listarSalones();
    }

    @GetMapping("/{id}")
    public Salon obtener(@PathVariable Long id) {
        return salonService.obtenerSalonPorId(id);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        salonService.eliminarSalon(id);
    }
}
