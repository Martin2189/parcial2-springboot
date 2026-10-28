
package com.parcial2.demo.Controllers;

import com.parcial2.demo.models.Tareas;
import com.parcial2.demo.Service.TareasService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tareas")
@RequiredArgsConstructor
public class TareasController {

    private final TareasService tareasService;

    @GetMapping
    public ResponseEntity<List<Tareas>> listarTodos() {
        return ResponseEntity.ok(tareasService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tareas> obtenerPorId(@PathVariable Long id) {
        return tareasService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Tareas tareas) {
        try {
            Tareas nuevaTarea = tareasService.guardar(tareas);

            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(nuevaTarea);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @PathVariable Long id,
            @RequestBody Tareas tareas) {

        try {
            Tareas tareaActualizada =
                    tareasService.actualizar(id, tareas);

            if (tareaActualizada != null) {
                return ResponseEntity.ok(tareaActualizada);
            }

            return ResponseEntity.notFound().build();

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        tareasService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
