package bookstore;

import bookstore.model.Book;
import bookstore.service.BookService;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class OnlineBookStoreAppApplication {
    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(OnlineBookStoreAppApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            Book book = new Book();
            book.setTitle("American Psycho");
            book.setAuthor("Bret Easton Ellis");
            book.setIsbn("9781613470190");
            book.setPrice(BigDecimal.valueOf(1.99));
            book.setDescription("Patrick Bateman is twenty-six and works on Wall Street. "
                        + "He is handsome, sophisticated, charming and intelligent. "
                        + "He is also a psychopath.");

            bookService.save(book);
            System.out.println("Books: " + bookService.findAll());
        };
    }
}
