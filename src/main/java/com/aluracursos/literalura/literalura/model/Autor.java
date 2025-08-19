package com.aluracursos.literalura.literalura.model;

import jakarta.persistence.*;
import java.util.Set;

@Entity
public class Autor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private Integer nacimiento;
    private Integer fallecimiento;

    @ManyToMany(mappedBy = "autores")
    private Set<Libro> libros;

    // Getters y setters
    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public Integer getNacimiento() { return nacimiento; }
    public void setNacimiento(Integer nacimiento) { this.nacimiento = nacimiento; }
    public Integer getFallecimiento() { return fallecimiento; }
    public void setFallecimiento(Integer fallecimiento) { this.fallecimiento = fallecimiento; }
    public Set<Libro> getLibros() { return libros; }
    public void setLibros(Set<Libro> libros) { this.libros = libros; }
}

