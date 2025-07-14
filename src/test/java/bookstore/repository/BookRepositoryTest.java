package bookstore.repository;

import static org.assertj.core.api.Assertions.assertThat;

import bookstore.model.Book;
import bookstore.repository.book.BookRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;

    @Test
    @DisplayName("findAllByCategories_Id should return correct books with specified categories")
    @Sql(scripts = {"classpath:database/books/create-books-table.sql",
            "classpath:database/categories/create-categories-table.sql",
            "classpath:database/categories/insert-2-categories.sql",
            "classpath:database/books/create-books_categories-table.sql",
            "classpath:database/books/insert-3-books.sql"},
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {"classpath:database/books/drop-books_categories-table.sql",
            "classpath:database/books/drop-books-table.sql",
            "classpath:database/categories/drop-categories-table.sql"},
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void findAllByCategories_Id_3Books_True() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> books = bookRepository.findAllByCategories_Id(1L, pageable);
        assertThat(books.getContent())
                .extracting(Book::getTitle)
                .doesNotContain("The Hitchhikers Guide to the Galaxy")
                .containsExactlyInAnyOrder("The Shining", "It");
    }
}
