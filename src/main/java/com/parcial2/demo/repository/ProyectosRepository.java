
package com.parcial2.demo.repository;

import com.parcial2.demo.models.Proyectos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProyectosRepository extends JpaRepository<Proyectos, Long> {

    long countByUsuario_IdAndActivoTrue(Long idUsuario);

    List<Proyectos> findByUsuario_Id(Long idUsuario);
}
