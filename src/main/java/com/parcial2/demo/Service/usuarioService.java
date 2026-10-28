
package com.parcial2.demo.Service;

import com.parcial2.demo.models.Usuario;
import com.parcial2.demo.models.enums.Rolusuario;
import com.parcial2.demo.repository.usuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class usuarioService {

    private final usuarioRepository UsuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public Usuario guardar(Usuario usuario) {
        usuario.setPassword(
                passwordEncoder.encode(usuario.getPassword())
        );

        if (usuario.getRol() == null) {
            usuario.setRol(Rolusuario.USER);
        }

        return UsuarioRepository.save(usuario);
    }

    public List<Usuario> obtenerTodos() {
        return UsuarioRepository.findAll();
    }

    public Optional<Usuario> obtenerPorId(Long id) {
        return UsuarioRepository.findById(id);
    }

    public void eliminar(Long id) {
        UsuarioRepository.deleteById(id);
    }

    public Usuario actualizar(Long id, Usuario usuarioActualizado) {
        Optional<Usuario> usuarioExistente =
                UsuarioRepository.findById(id);

        if (usuarioExistente.isPresent()) {
            Usuario usuario = usuarioExistente.get();

            usuario.setUsername(usuarioActualizado.getUsername());
            usuario.setActivo(usuarioActualizado.getActivo());

            if (usuarioActualizado.getPassword() != null
                    && !usuarioActualizado.getPassword().isBlank()) {
                usuario.setPassword(
                        passwordEncoder.encode(
                                usuarioActualizado.getPassword()
                        )
                );
            }

            return UsuarioRepository.save(usuario);
        }

        return null;
    }

    public Usuario login(String username, String passwordPlano) {
        Optional<Usuario> usuarioOpt =
                UsuarioRepository.findByUsername(username);

        if (usuarioOpt.isPresent()) {
            Usuario usuario = usuarioOpt.get();

            if (usuario.getActivo()
                    && passwordEncoder.matches(
                    passwordPlano, usuario.getPassword())) {
                return usuario;
            }
        }

        return null;
    }

    // Buscar usuario por nombre de usuario
    public Optional<Usuario> obtenerPorUsername(String username) {
        return UsuarioRepository.findByUsername(username);
    }
}


