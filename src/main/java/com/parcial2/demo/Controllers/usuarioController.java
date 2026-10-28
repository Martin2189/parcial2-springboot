package com.parcial2.demo.Controllers;


import com.parcial2.demo.models.Usuario;
import com.parcial2.demo.Service.usuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor

public class usuarioController {

    private final usuarioService Usurioservice;

    @GetMapping
    public ResponseEntity<List<Usuario>> listarTodos() {
        return ResponseEntity.ok(
                Usurioservice.obtenerTodos()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerPorId(
            @PathVariable Long id) {

        return Usurioservice.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Usuario> crear(
            @RequestBody Usuario usuario) {

        Usuario nuevoUsuario =
                Usurioservice.guardar(usuario);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(nuevoUsuario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> actualizar(
            @PathVariable Long id,
            @RequestBody Usuario usuario) {

        Usuario actualizado =
                Usurioservice.actualizar(id, usuario);

        if (actualizado != null) {
            return ResponseEntity.ok(actualizado);
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @PathVariable Long id) {

        Usurioservice.eliminar(id);

        return ResponseEntity.noContent().build();
    }





}
