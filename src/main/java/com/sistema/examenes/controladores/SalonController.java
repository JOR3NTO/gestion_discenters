package com.sistema.examenes.controladores;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.sistema.examenes.entidades.Salon;
import com.sistema.examenes.servicios.SalonService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/salones")
public class SalonController {

    private final SalonService salonService;

    public SalonController(SalonService salonService) {
        this.salonService = salonService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Salon registrar(@Valid @RequestBody Salon salon) {
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

    @PutMapping("/{id}")
    public Salon actualizar(@PathVariable Long id, @Valid @RequestBody Salon salon) {
        return salonService.actualizarSalon(id, salon);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        salonService.eliminarSalon(id);
    }
}
