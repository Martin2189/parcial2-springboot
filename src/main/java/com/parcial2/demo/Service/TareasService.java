
package com.parcial2.demo.Service;

import com.parcial2.demo.models.Tareas;
import com.parcial2.demo.repository.TareasRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TareasService {

    private final TareasRepository tareasRepository;

    public Tareas guardar(Tareas tareas) {
        if (tareas.getProyecto() == null
                || tareas.getProyecto().getIdproyectos() == null) {
            throw new IllegalArgumentException(
                    "La tarea debe estar asociada a un proyecto válido"
            );
        }

        if (tareas.getEstado() == null) {
            tareas.setEstado(
                    com.parcial2.demo.models.enums.Estado.PENDIENTE
            );
        }

        if (tareas.getActivo() == null) {
            tareas.setActivo(true);
        }

        return tareasRepository.save(tareas);
    }

    public List<Tareas> obtenerTodos() {
        return tareasRepository.findAll();
    }

    public Optional<Tareas> obtenerPorId(Long id) {
        return tareasRepository.findById(id);
    }

    public void eliminar(Long id) {
        tareasRepository.deleteById(id);
    }

    public Tareas actualizar(Long idTareas, Tareas tareasActualizado) {
        Optional<Tareas> tareaExistente =
                tareasRepository.findById(idTareas);

        if (tareaExistente.isPresent()) {
            Tareas tarea = tareaExistente.get();

            if (tareasActualizado.getEstado() == null) {
                throw new IllegalArgumentException(
                        "El estado de la tarea es obligatorio"
                );
            }

            tarea.setEstado(tareasActualizado.getEstado());

            return tareasRepository.save(tarea);
        }

        return null;
    }
}


