
package com.parcial2.demo.Controllers;

import com.parcial2.demo.models.Proyectos;
import com.parcial2.demo.models.Usuario;
import com.parcial2.demo.Service.ProyectosService;
import com.parcial2.demo.Service.usuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proyectos")
@RequiredArgsConstructor
public class ProyectosController {

    private final ProyectosService proyectosService;
    private final usuarioService usuarioService;

    // Consultar únicamente los proyectos del usuario autenticado
    @GetMapping
    public ResponseEntity<?> listarTodos(Authentication authentication) {

        if (authentication == null ||
                !authentication.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Debes iniciar sesión para consultar tus proyectos");
        }

        Usuario usuario = usuarioService
                .obtenerPorUsername(authentication.getName())
                .orElse(null);

        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Usuario autenticado no encontrado");
        }

        List<Proyectos> proyectos =
                proyectosService.obtenerPorUsuario(usuario.getId());

        return ResponseEntity.ok(proyectos);
    }

    // Consultar un proyecto por ID
    @GetMapping("/{id}")
    public ResponseEntity<Proyectos> obtenerPorId(
            @PathVariable Long id) {

        return proyectosService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Crear un proyecto asociado al usuario del JWT
    @PostMapping
    public ResponseEntity<?> crear(
            @RequestBody Proyectos proyectos,
            Authentication authentication) {

        try {
            if (authentication == null ||
                    !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("Debes iniciar sesión");
            }

            Usuario usuario = usuarioService
                    .obtenerPorUsername(authentication.getName())
                    .orElseThrow(() ->
                            new IllegalArgumentException(
                                    "Usuario autenticado no encontrado"));

            // El usuario se obtiene del token, no del JSON
            proyectos.setUsuario(usuario);

            Proyectos nuevoProyecto =
                    proyectosService.guardar(proyectos);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(nuevoProyecto);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Actualizar un proyecto
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @PathVariable Long id,
            @RequestBody Proyectos proyectos) {

        try {
            Proyectos proyectoActualizado =
                    proyectosService.actualizar(id, proyectos);

            if (proyectoActualizado != null) {
                return ResponseEntity.ok(proyectoActualizado);
            }

            return ResponseEntity.notFound().build();

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Eliminar un proyecto
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {

        proyectosService.eliminar(id);

        return ResponseEntity.noContent().build();
    }
}

