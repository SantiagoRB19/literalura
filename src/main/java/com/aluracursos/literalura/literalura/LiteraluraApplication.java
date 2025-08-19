package com.aluracursos.literalura.literalura;

import com.aluracursos.literalura.literalura.model.Libro;
import com.aluracursos.literalura.literalura.repository.AutorRepository;
import com.aluracursos.literalura.literalura.repository.LibroRepository;
import com.aluracursos.literalura.literalura.service.GutendexService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class LiteraluraApplication implements CommandLineRunner {

    private final GutendexService gutendexService;
    private final LibroRepository libroRepository;
    private final AutorRepository autorRepository;

    public LiteraluraApplication(GutendexService gutendexService,
                                 LibroRepository libroRepository,
                                 AutorRepository autorRepository) {
        this.gutendexService = gutendexService;
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(LiteraluraApplication.class, args);
    }

    @Override
    public void run(String... args) {
        Scanner sc = new Scanner(System.in);
        int opcion = -1;

        do {
            System.out.println("\n--- MENÚ ---");
            System.out.println("1- Buscar libro por título");
            System.out.println("2- Listar libros registrados");
            System.out.println("3- Listar autores registrados");
            System.out.println("4- Listar autores vivos en un determinado año");
            System.out.println("5- Listar libros por idioma");
            System.out.println("0- Salir");
            System.out.print("Elija opción: ");

            String linea = sc.nextLine();
            try {
                opcion = Integer.parseInt(linea);
            } catch (NumberFormatException e) {
                opcion = -1;
            }

            switch (opcion) {
                case 1 -> {
                    System.out.print("Ingrese título: ");
                    String titulo = sc.nextLine();
                    Libro libro = gutendexService.buscarPorTitulo(titulo);
                    if (libro == null) {
                        System.out.println("No encontrado.");
                    }
                }
                case 2 -> libroRepository.findAll().forEach(l -> System.out.println(l.getTitulo()));
                case 3 -> autorRepository.findAll().forEach(a -> System.out.println(a.getNombre()));
                case 4 -> {
                    System.out.print("Ingrese año: ");
                    String añoStr = sc.nextLine();
                    try {
                        int year = Integer.parseInt(añoStr);
                        autorRepository.findAll().stream()
                                .filter(a -> a.getNacimiento() != null && a.getNacimiento() <= year &&
                                        (a.getFallecimiento() == null || a.getFallecimiento() >= year))
                                .forEach(a -> System.out.println(a.getNombre()));
                    } catch (NumberFormatException ex) {
                        System.out.println("Año inválido.");
                    }
                }
                case 5 -> {
                    System.out.print("Ingrese idioma (ej: en, es, fr): ");
                    String idioma = sc.nextLine();
                    libroRepository.findByIdioma(idioma).forEach(l -> System.out.println(l.getTitulo()));
                }
                case 0 -> System.out.println("Saliendo...");
                default -> System.out.println("Opción inválida. Intente de nuevo.");
            }

        } while (opcion != 0);
    }
}
