package com.sistema.examenes.servicios.impl;

import java.util.List;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sistema.examenes.dto.UsuarioRequest;
import com.sistema.examenes.entidades.Rol;
import com.sistema.examenes.entidades.Usuario;
import com.sistema.examenes.entidades.UsuarioRol;
import com.sistema.examenes.excepciones.RecursoNoEncontradoException;
import com.sistema.examenes.excepciones.ReglaNegocioException;
import com.sistema.examenes.repositorios.RolRepository;
import com.sistema.examenes.repositorios.UsuarioRepository;
import com.sistema.examenes.servicios.UsuarioService;

@Service
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, RolRepository rolRepository,
            PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.rolRepository = rolRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Usuario registrarUsuario(UsuarioRequest request) {
        if (usuarioRepository.existsByUsername(request.username())) {
            throw new ReglaNegocioException("El nombre de usuario " + request.username() + " ya existe");
        }
        if (usuarioRepository.existsByEmail(request.email())) {
            throw new ReglaNegocioException("El email " + request.email() + " ya está registrado");
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(request.username());
        usuario.setPassword(passwordEncoder.encode(request.password()));
        usuario.setNombre(request.nombre());
        usuario.setApellido(request.apellido());
        usuario.setEmail(request.email());
        usuario.setTelefono(request.telefono());
        usuario.setPerfil(request.perfil());

        Set<Long> rolIds = request.rolIds() == null ? Set.of() : request.rolIds();
        for (Long rolId : rolIds) {
            Rol rol = rolRepository.findById(rolId)
                    .orElseThrow(() -> new RecursoNoEncontradoException("Rol", rolId));
            UsuarioRol usuarioRol = new UsuarioRol();
            usuarioRol.setUsuario(usuario);
            usuarioRol.setRol(rol);
            usuario.getUsuarioRoles().add(usuarioRol);
        }
        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario obtenerUsuarioPorId(Long id) {
        return usuarioRepository.findById(id).orElseThrow(() -> new RecursoNoEncontradoException("Usuario", id));
    }

    @Override
    public void eliminarUsuario(Long id) {
        usuarioRepository.delete(obtenerUsuarioPorId(id));
    }
}
