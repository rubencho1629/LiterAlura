package com.OneOracle.LiterAlura;

import com.OneOracle.LiterAlura.model.Author;
import com.OneOracle.LiterAlura.model.Book;
import com.OneOracle.LiterAlura.service.GutendexService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class LiterAluraApplication implements CommandLineRunner {

	private final GutendexService gutendexService;

	public LiterAluraApplication(GutendexService gutendexService) {
		this.gutendexService = gutendexService;
	}

	public static void main(String[] args) {
		SpringApplication.run(LiterAluraApplication.class, args);
	}

	@Override
	public void run(String... args) {
		Scanner scanner = new Scanner(System.in);
		int option;

		do {
			displayMenu();
			System.out.print("Seleccione una opción: ");
			while (!scanner.hasNextInt()) {
				System.out.println("Por favor, introduzca un número válido.");
				scanner.next(); // Descartar entrada inválida
			}
			option = scanner.nextInt();
			scanner.nextLine(); // Consumir nueva línea

			switch (option) {
				case 1 -> showAllBooks();
				case 2 -> searchBooksByAuthor(scanner);
				case 3 -> sortBooksByDownloads();
				case 4 -> searchBookByTitle(scanner);
				case 5 -> filterBooksByLanguage(scanner);
				case 6 -> listAllAuthors();
				case 7 -> listAuthorsAliveInYear(scanner);
				case 8 -> System.out.println("¡Gracias por usar LiterAlura! Saliendo...");
				default -> System.out.println("Opción inválida. Inténtalo de nuevo.");
			}
		} while (option != 8);
	}

	private void displayMenu() {
		System.out.println("\n==== Menú LiterAlura ====");
		System.out.println("1. Mostrar todos los libros");
		System.out.println("2. Buscar libros por autor");
		System.out.println("3. Ordenar libros por número de descargas");
		System.out.println("4. Buscar un libro por título");
		System.out.println("5. Filtrar libros por idioma");
		System.out.println("6. Listar todos los autores");
		System.out.println("7. Listar autores vivos en un año");
		System.out.println("8. Salir");
		System.out.println("=========================");
	}

	private void showAllBooks() {
		List<Book> books = gutendexService.fetchBooks();
		System.out.println("\n==== Lista de Libros ====");
		books.forEach(book -> System.out.println("ID: " + book.getId() + ", Título: " + book.getTitle()));
	}

	private void searchBooksByAuthor(Scanner scanner) {
		System.out.print("Ingrese el nombre del autor: ");
		String authorName = scanner.nextLine();
		List<Book> books = gutendexService.filterBooksByAuthor(authorName);

		if (books.isEmpty()) {
			System.out.println("No se encontraron libros para el autor: " + authorName);
		} else {
			System.out.println("\n==== Libros de " + authorName + " ====");
			books.forEach(book -> System.out.println("ID: " + book.getId() + ", Título: " + book.getTitle()));
		}
	}

	private void sortBooksByDownloads() {
		List<Book> books = gutendexService.sortBooksByDownloads();
		System.out.println("\n==== Libros Ordenados por Descargas ====");
		books.forEach(book -> System.out.println("Título: " + book.getTitle() + ", Descargas: " + book.getDownloadCount()));
	}

	private void searchBookByTitle(Scanner scanner) {
		System.out.print("Ingrese el título del libro: ");
		String title = scanner.nextLine();
		Book book = gutendexService.fetchBookByTitle(title);

		if (book != null) {
			System.out.println("\n==== Libro Encontrado ====");
			System.out.println("Título: " + book.getTitle());
			System.out.println("Autor: " + book.getAuthors().get(0).getName());
			System.out.println("Idioma: " + book.getLanguages().get(0));
			System.out.println("Descargas: " + book.getDownloadCount());
		} else {
			System.out.println("No se encontró ningún libro con el título: " + title);
		}
	}

	private void filterBooksByLanguage(Scanner scanner) {
		System.out.print("Ingrese el idioma (código ISO 639-1, por ejemplo, 'en' para inglés): ");
		String language = scanner.nextLine();
		List<Book> books = gutendexService.filterBooksByLanguage(language);

		if (books.isEmpty()) {
			System.out.println("No se encontraron libros en el idioma: " + language);
		} else {
			System.out.println("\n==== Libros en Idioma " + language + " ====");
			books.forEach(book -> System.out.println("Título: " + book.getTitle() + ", Autor: " + book.getAuthors().get(0).getName()));
		}
	}

	private void listAllAuthors() {
		List<Author> authors = gutendexService.listAllAuthors();
		System.out.println("\n==== Lista de Autores ====");
		authors.forEach(author -> System.out.println("Nombre: " + author.getName() +
				", Año de nacimiento: " + author.getBirthYear() +
				", Año de fallecimiento: " + (author.getDeathYear() != null ? author.getDeathYear() : "N/A")));
	}

	private void listAuthorsAliveInYear(Scanner scanner) {
		System.out.print("Ingrese el año: ");
		while (!scanner.hasNextInt()) {
			System.out.println("Por favor, introduzca un número válido.");
			scanner.next();
		}
		int year = scanner.nextInt();
		scanner.nextLine(); // Consumir nueva línea

		List<Author> authors = gutendexService.listAuthorsAliveInYear(year);
		if (authors.isEmpty()) {
			System.out.println("No se encontraron autores vivos en el año " + year + ".");
		} else {
			System.out.println("\n==== Autores Vivos en " + year + " ====");
			authors.forEach(author -> System.out.println("Nombre: " + author.getName() +
					", Año de nacimiento: " + author.getBirthYear() +
					", Año de fallecimiento: " + (author.getDeathYear() != null ? author.getDeathYear() : "N/A")));
		}
	}
}
