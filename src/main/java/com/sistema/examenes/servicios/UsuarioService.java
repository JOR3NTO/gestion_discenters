package com.sistema.examenes.servicios;

import java.util.List;

import com.sistema.examenes.dto.UsuarioRequest;
import com.sistema.examenes.entidades.Usuario;

public interface UsuarioService {
    Usuario registrarUsuario(UsuarioRequest request);
    List<Usuario> listarUsuarios();
    Usuario obtenerUsuarioPorId(Long id);
    void eliminarUsuario(Long id);
}
