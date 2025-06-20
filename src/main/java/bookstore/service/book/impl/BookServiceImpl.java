package bookstore.service.book.impl;

import bookstore.dto.book.BookDto;
import bookstore.dto.book.BookSearchParametersDto;
import bookstore.dto.book.CreateBookRequestDto;
import bookstore.exception.EntityNotFoundException;
import bookstore.mapper.BookMapper;
import bookstore.model.Book;
import bookstore.repository.book.BookRepository;
import bookstore.repository.book.BookSpecificationBuilder;
import bookstore.service.book.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Page<BookDto> findAll(Pageable pageable) {
        return bookRepository.findAll(pageable)
                .map(bookMapper::toBookDto);
    }

    @Override
    public Page<BookDto> search(BookSearchParametersDto params, Pageable pageable) {
        return bookRepository.findAll(bookSpecificationBuilder.build(params), pageable)
                .map(bookMapper::toBookDto);
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
