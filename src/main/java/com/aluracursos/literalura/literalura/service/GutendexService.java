package com.aluracursos.literalura.literalura.service;

import com.aluracursos.literalura.literalura.model.Autor;
import com.aluracursos.literalura.literalura.model.Libro;
import com.aluracursos.literalura.literalura.repository.LibroRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

@Service
public class GutendexService {

    private final LibroRepository libroRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();

    public GutendexService(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }

    public Libro buscarPorTitulo(String titulo) {
        try {
            String url = "https://gutendex.com/books/?search=" + URLEncoder.encode(titulo, StandardCharsets.UTF_8);
            System.out.println("Buscando en: " + url);

            String response = restTemplate.getForObject(url, String.class);
            JsonNode root = mapper.readTree(response);
            JsonNode results = root.path("results");

            if (!results.isArray() || results.size() == 0) {
                return null;
            }

            JsonNode bookNode = results.get(0);
            Libro libro = new Libro();
            libro.setTitulo(bookNode.path("title").asText());

            if (bookNode.path("languages").isArray() && bookNode.path("languages").size() > 0) {
                libro.setIdioma(bookNode.path("languages").get(0).asText());
            }

            int descargas = bookNode.path("download_count").asInt(0);

            Set<Autor> autores = new HashSet<>();
            for (JsonNode autorNode : bookNode.path("authors")) {
                Autor autor = new Autor();
                autor.setNombre(autorNode.path("name").asText());
                autor.setNacimiento(autorNode.path("birth_year").isNull() ? null : autorNode.path("birth_year").asInt());
                autor.setFallecimiento(autorNode.path("death_year").isNull() ? null : autorNode.path("death_year").asInt());
                autores.add(autor);
            }

            libro.setAutores(autores);
            libroRepository.save(libro);

            // Mostrar info formateada
            System.out.println("\nTítulo: " + libro.getTitulo());
            System.out.print("Autor(es): ");
            libro.getAutores().forEach(a -> System.out.print(a.getNombre() + " "));
            System.out.println("\nIdioma: " + libro.getIdioma());
            System.out.println("Número de descargas: " + descargas);

            return libro;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
