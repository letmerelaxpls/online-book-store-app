package bookstore.service.impl;

import bookstore.dto.BookDto;
import bookstore.dto.BookSearchParametersDto;
import bookstore.dto.CreateBookRequestDto;
import bookstore.exception.EntityNotFoundException;
import bookstore.mapper.BookMapper;
import bookstore.model.Book;
import bookstore.repository.book.BookRepository;
import bookstore.repository.book.BookSpecificationBuilder;
import bookstore.service.BookService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final BookSpecificationBuilder bookSpecificationBuilder;

    @Override
    public BookDto save(CreateBookRequestDto bookDto) {
        return bookMapper.toBookDto(
                bookRepository.save(
                        bookMapper.toBook(bookDto)));
    }

    @Override
    public BookDto findById(Long id) {
        return bookMapper.toBookDto(
                bookRepository.findById(id)
                        .orElseThrow(() ->
                                new EntityNotFoundException("Could not find a Book with id: "
                                        + id)));
    }

    @Override
    public List<BookDto> search(BookSearchParametersDto params) {
        return bookRepository.findAll(bookSpecificationBuilder.build(params))
                .stream()
                .map(bookMapper::toBookDto)
                .toList();
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository.findAll()
                .stream()
                .map(bookMapper::toBookDto)
                .toList();
    }

    @Override
    public BookDto update(Long id, CreateBookRequestDto bookDto) {
        Book book = bookRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException("Could not find a Book with id: "
                        + id));
        bookMapper.updateBookFromDto(book, bookDto);
        return bookMapper.toBookDto(bookRepository.save(book));
    }

    @Override
    public void delete(Long id) {
        bookRepository.deleteById(id);
    }
}
