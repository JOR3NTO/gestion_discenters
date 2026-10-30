package com.sistema.examenes.configuracion;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sistema.examenes.entidades.Rol;
import com.sistema.examenes.repositorios.RolRepository;

/** Crea los roles básicos la primera vez que arranca la aplicación. */
@Configuration
public class RolesIniciales {

    @Bean
    CommandLineRunner cargarRoles(RolRepository rolRepository) {
        return args -> List.of(new Rol(1L, "ADMIN"), new Rol(2L, "PROFESOR"), new Rol(3L, "ESTUDIANTE"))
                .forEach(rol -> {
                    if (!rolRepository.existsById(rol.getRolId())) {
                        rolRepository.save(rol);
                    }
                });
    }
}
