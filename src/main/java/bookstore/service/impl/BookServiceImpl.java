package bookstore.service.impl;

import bookstore.dto.BookDto;
import bookstore.dto.CreateBookRequestDto;
import bookstore.exception.EntityNotFoundException;
import bookstore.mapper.BookMapper;
import bookstore.repository.BookRepository;
import bookstore.service.BookService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;

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
    public List<BookDto> findAll() {
        return bookRepository.findAll().stream()
                .map(bookMapper::toBookDto)
                .toList();
    }
}
