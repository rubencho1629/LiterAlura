# LiterAlura

## Description
LiterAlura is a Spring Boot application that interacts with the Gutendex API to fetch and manage books and authors. It provides various functionalities such as listing authors, filtering books by author or language, and sorting books by download count.

## Technologies
- Java
- Spring Boot
- Maven

## Setup

### Prerequisites
- Java 11 or higher
- Maven 3.6.3 or higher

### Installation
1. Clone the repository:
    ```sh
    git clone https://github.com/yourusername/LiterAlura.git
    cd LiterAlura
    ```

2. Build the project:
    ```sh
    mvn clean install
    ```

3. Run the application:
    ```sh
    mvn spring-boot:run
    ```

## Usage

### Fetch Books
To fetch books from the Gutendex API and save them to the database, use the `GutendexService` class.

### List All Authors
To list all authors in the database, use the `listAllAuthors` method in the `GutendexService` class.

### List Authors Alive in a Specific Year
To list authors who were alive in a specific year, use the `listAuthorsAliveInYear` method in the `GutendexService` class.

### Filter Books by Author
To filter books by a specific author, use the `filterBooksByAuthor` method in the `GutendexService` class.

### Filter Books by Language
To filter books by language, use the `filterBooksByLanguage` method in the `GutendexService` class.

### Sort Books by Downloads
To sort books by the number of downloads, use the `sortBooksByDownloads` method in the `GutendexService` class.

### Get Books Statistics
To get statistics of books by languages, use the `getBooksStatistics` method in the `GutendexService` class.

## Contributing
1. Fork the repository.
2. Create a new branch (`git checkout -b feature-branch`).
3. Make your changes.
4. Commit your changes (`git commit -m 'Add some feature'`).
5. Push to the branch (`git push origin feature-branch`).
6. Open a pull request.

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## APIs

