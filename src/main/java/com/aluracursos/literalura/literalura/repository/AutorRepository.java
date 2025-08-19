package com.aluracursos.literalura.literalura.repository;

import com.aluracursos.literalura.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AutorRepository extends JpaRepository<Autor, Long> {
    List<Autor> findByNacimientoLessThanEqualAndFallecimientoGreaterThanEqual(Integer year1, Integer year2);
}

