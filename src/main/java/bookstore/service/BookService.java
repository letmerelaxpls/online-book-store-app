package bookstore.service;

import bookstore.dto.BookDto;
import bookstore.dto.BookSearchParametersDto;
import bookstore.dto.CreateBookRequestDto;
import java.util.List;

public interface BookService {
    BookDto save(CreateBookRequestDto book);

    BookDto findById(Long id);

    List<BookDto> search(BookSearchParametersDto params);

    List<BookDto> findAll();

    BookDto update(Long id, CreateBookRequestDto bookDto);

    void delete(Long id);
}
