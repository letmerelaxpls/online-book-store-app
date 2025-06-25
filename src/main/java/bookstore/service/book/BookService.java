package bookstore.service.book;

import bookstore.dto.book.BookDto;
import bookstore.dto.book.BookSearchParametersDto;
import bookstore.dto.book.CreateBookRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    BookDto save(CreateBookRequestDto book);

    BookDto findById(Long id);

    Page<BookDto> search(BookSearchParametersDto params, Pageable pageable);

    Page<BookDto> findAll(Pageable pageable);

    BookDto update(Long id, CreateBookRequestDto bookDto);

    void delete(Long id);
}
