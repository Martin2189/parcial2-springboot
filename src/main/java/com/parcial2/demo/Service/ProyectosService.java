
package com.parcial2.demo.Service;

import com.parcial2.demo.models.Proyectos;
import com.parcial2.demo.repository.ProyectosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProyectosService {

    private final ProyectosRepository proyectosRepository;

    public Proyectos guardar(Proyectos proyectos) {

        if (proyectos.getUsuario() == null
                || proyectos.getUsuario().getId() == null) {
            throw new IllegalArgumentException(
                    "El proyecto debe tener un usuario"
            );
        }

        if (proyectos.getActivo() == null) {
            proyectos.setActivo(true);
        }

        Long idUsuario = proyectos.getUsuario().getId();

        if (Boolean.TRUE.equals(proyectos.getActivo())) {
            long proyectosActivos =
                    proyectosRepository.countByUsuario_IdAndActivoTrue(idUsuario);

            if (proyectosActivos >= 3) {
                throw new IllegalArgumentException(
                        "El usuario no puede tener más de 3 proyectos activos"
                );
            }
        }

        return proyectosRepository.save(proyectos);
    }

    public List<Proyectos> obtenerTodos() {
        return proyectosRepository.findAll();
    }

    public List<Proyectos> obtenerPorUsuario(Long idUsuario) {
        return proyectosRepository.findByUsuario_Id(idUsuario);
    }

    public Optional<Proyectos> obtenerPorId(Long id) {
        return proyectosRepository.findById(id);
    }

    public void eliminar(Long id) {
        proyectosRepository.deleteById(id);
    }

    public Proyectos actualizar(Long idproyectos,
                                Proyectos proyectosActualizado) {

        Optional<Proyectos> proyectoExistente =
                proyectosRepository.findById(idproyectos);

        if (proyectoExistente.isPresent()) {

            Proyectos proyecto = proyectoExistente.get();

            if (proyectosActualizado.getActivo() == null) {
                proyectosActualizado.setActivo(proyecto.getActivo());
            }

            boolean seActivara =
                    !Boolean.TRUE.equals(proyecto.getActivo())
                            && Boolean.TRUE.equals(proyectosActualizado.getActivo());

            if (seActivara) {

                long proyectosActivos =
                        proyectosRepository.countByUsuario_IdAndActivoTrue(
                                proyecto.getUsuario().getId()
                        );

                if (proyectosActivos >= 3) {
                    throw new IllegalArgumentException(
                            "El usuario no puede tener más de 3 proyectos activos"
                    );
                }
            }

            proyecto.setNombre(proyectosActualizado.getNombre());
            proyecto.setDescripcion(proyectosActualizado.getDescripcion());
            proyecto.setActivo(proyectosActualizado.getActivo());

            return proyectosRepository.save(proyecto);
        }

        return null;
    }
}
