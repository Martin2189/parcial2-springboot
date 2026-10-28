
package com.parcial2.demo.repository;

import com.parcial2.demo.models.Tareas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TareasRepository extends JpaRepository<Tareas, Long> {
}

