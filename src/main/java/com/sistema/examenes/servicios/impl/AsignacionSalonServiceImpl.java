package com.sistema.examenes.servicios.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sistema.examenes.dto.AsignacionRequest;
import com.sistema.examenes.entidades.AsignacionSalon;
import com.sistema.examenes.entidades.Estudiante;
import com.sistema.examenes.entidades.HorarioClase;
import com.sistema.examenes.entidades.Salon;
import com.sistema.examenes.excepciones.RecursoNoEncontradoException;
import com.sistema.examenes.excepciones.ReglaNegocioException;
import com.sistema.examenes.repositorios.AsignacionSalonRepository;
import com.sistema.examenes.servicios.EstudianteService;
import com.sistema.examenes.servicios.HorarioClaseService;
import com.sistema.examenes.servicios.AsignacionSalonService;
import com.sistema.examenes.servicios.SalonService;

/**
 * Reglas de negocio:
 * - Un salón no puede tener dos asignaciones cuyos horarios se crucen.
 * - Un salón no admite más estudiantes que su capacidad.
 * - Un estudiante no puede estar en dos asignaciones cuyos horarios se crucen.
 */
@Service
@Transactional
public class AsignacionSalonServiceImpl implements AsignacionSalonService {

    private final AsignacionSalonRepository asignacionRepository;
    private final SalonService salonService;
    private final HorarioClaseService horarioService;
    private final EstudianteService estudianteService;

    public AsignacionSalonServiceImpl(AsignacionSalonRepository asignacionRepository, SalonService salonService,
            HorarioClaseService horarioService, EstudianteService estudianteService) {
        this.asignacionRepository = asignacionRepository;
        this.salonService = salonService;
        this.horarioService = horarioService;
        this.estudianteService = estudianteService;
    }

    @Override
    public AsignacionSalon registrarAsignacion(AsignacionRequest request) {
        Salon salon = salonService.obtenerSalonPorId(request.salonId());
        HorarioClase horario = horarioService.obtenerHorarioPorId(request.horarioId());

        boolean ocupado = asignacionRepository.findBySalonId(salon.getId()).stream()
                .anyMatch(a -> a.getHorario().seCruzaCon(horario));
        if (ocupado) {
            throw new ReglaNegocioException("El salón " + salon.getNombre() + " ya está ocupado en ese horario");
        }

        AsignacionSalon asignacion = new AsignacionSalon();
        asignacion.setSalon(salon);
        asignacion.setHorario(horario);
        return asignacionRepository.save(asignacion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AsignacionSalon> listarAsignaciones() {
        return asignacionRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AsignacionSalon> listarPorSalon(Long salonId) {
        salonService.obtenerSalonPorId(salonId);
        return asignacionRepository.findBySalonId(salonId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AsignacionSalon> listarPorEstudiante(Long estudianteId) {
        estudianteService.obtenerEstudiantePorId(estudianteId);
        return asignacionRepository.findByEstudiantesId(estudianteId);
    }

    @Override
    @Transactional(readOnly = true)
    public AsignacionSalon obtenerAsignacionPorId(Long id) {
        return asignacionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Asignación", id));
    }

    @Override
    public void eliminarAsignacion(Long id) {
        asignacionRepository.delete(obtenerAsignacionPorId(id));
    }

    @Override
    public AsignacionSalon inscribirEstudiante(Long asignacionId, Long estudianteId) {
        AsignacionSalon asignacion = obtenerAsignacionPorId(asignacionId);
        Estudiante estudiante = estudianteService.obtenerEstudiantePorId(estudianteId);

        if (asignacion.getEstudiantes().contains(estudiante)) {
            throw new ReglaNegocioException("El estudiante ya está inscrito en esta asignación");
        }
        if (asignacion.getEstudiantes().size() >= asignacion.getSalon().getCapacidad()) {
            throw new ReglaNegocioException("El salón " + asignacion.getSalon().getNombre() + " alcanzó su capacidad");
        }
        boolean cruce = asignacionRepository.findByEstudiantesId(estudianteId).stream()
                .anyMatch(a -> a.getHorario().seCruzaCon(asignacion.getHorario()));
        if (cruce) {
            throw new ReglaNegocioException("El estudiante ya tiene una clase que se cruza con ese horario");
        }

        asignacion.getEstudiantes().add(estudiante);
        return asignacionRepository.save(asignacion);
    }

    @Override
    public AsignacionSalon retirarEstudiante(Long asignacionId, Long estudianteId) {
        AsignacionSalon asignacion = obtenerAsignacionPorId(asignacionId);
        if (!asignacion.getEstudiantes().removeIf(e -> e.getId().equals(estudianteId))) {
            throw new RecursoNoEncontradoException("Inscripción del estudiante", estudianteId);
        }
        return asignacionRepository.save(asignacion);
    }
}
