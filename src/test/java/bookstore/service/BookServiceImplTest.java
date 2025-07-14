package bookstore.service;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import bookstore.dto.book.BookDto;
import bookstore.dto.book.BookSearchParametersDto;
import bookstore.dto.book.CreateBookRequestDto;
import bookstore.mapper.BookMapper;
import bookstore.model.Book;
import bookstore.repository.book.BookRepository;
import bookstore.repository.book.BookSpecificationBuilder;
import bookstore.service.book.impl.BookServiceImpl;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {
    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;
    @Mock
    private BookSpecificationBuilder specificationBuilder;
    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    @DisplayName("findAll should return all books")
    void findAll_2Books_True() {
        //given
        List<Book> books = createListOf2Books();
        List<BookDto> bookDtos = createListOf2BookDtos();
        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> bookPage = new PageImpl<>(books, pageable, 2);
        //when
        when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        when(bookMapper.toBookDto(books.get(0))).thenReturn(bookDtos.get(0));
        when(bookMapper.toBookDto(books.get(1))).thenReturn(bookDtos.get(1));
        Page<BookDto> result = bookService.findAll(pageable);
        //then
        Page<BookDto> expectedBookPage = new PageImpl<>(bookDtos, pageable, 2);
        Assertions.assertEquals(expectedBookPage, result);
    }

    @Test
    @DisplayName("findById should return correct book")
    void findById_BookWithId1_True() {
        //given
        Long id = 1L;
        String title = "Title";
        Book book = new Book();
        book.setId(id);
        book.setTitle(title);
        BookDto expectedBookDto = new BookDto();
        expectedBookDto.setId(id);
        expectedBookDto.setTitle(title);
        //when
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        when(bookMapper.toBookDto(book)).thenReturn(expectedBookDto);
        BookDto result = bookService.findById(1L);
        //then
        Assertions.assertEquals(expectedBookDto, result);
    }

    @Test
    @DisplayName("save should return correct book dto")
    void save_BookRequestDtoWithId1_True() {
        //given
        CreateBookRequestDto bookRequestDto = new CreateBookRequestDto();
        bookRequestDto.setTitle("Title");
        Book book = new Book();
        book.setId(1L);
        BookDto expectedBookDto = new BookDto();
        expectedBookDto.setId(1L);
        //when
        when(bookMapper.toBook(bookRequestDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toBookDto(book)).thenReturn(expectedBookDto);
        BookDto result = bookService.save(bookRequestDto);
        //then
        Assertions.assertEquals(expectedBookDto, result);
    }

    @Test
    @DisplayName("search should return correct book with specified parameters")
    void search_BookWithAuthorMe() {
        //given
        List<Book> books = createListOf2Books();
        List<BookDto> bookDtos = createListOf2BookDtos();
        BookSearchParametersDto searchParams = new BookSearchParametersDto(
                null, new String[]{"Me"}, null
        );
        Specification<Book> spec = (root,
                                    query,
                                    criteriaBuilder) -> null;
        Pageable pageable = PageRequest.of(0, 10);
        Page<Book> bookPage = new PageImpl<>(books, pageable, 2);
        //when
        when(specificationBuilder.build(searchParams)).thenReturn(spec);
        when(bookRepository.findAll(spec, pageable)).thenReturn(bookPage);
        when(bookMapper.toBookDto(books.get(0))).thenReturn(bookDtos.get(0));
        when(bookMapper.toBookDto(books.get(1))).thenReturn(bookDtos.get(1));
        //then
        Page<BookDto> expectedBookPage = new PageImpl<>(bookDtos, pageable, 2);
        Page<BookDto> result = bookService.search(searchParams, pageable);
        Assertions.assertEquals(expectedBookPage, result);
    }

    @Test
    @DisplayName("update should return correct book dto")
    void update_BookRequestDtoChangeTitle_True() {
        //given
        Long id = 1L;
        Book book = new Book();
        book.setId(id);
        book.setTitle("History");
        Book changedBook = new Book();
        changedBook.setId(id);
        String newTitle = "NewTitle";
        changedBook.setTitle(newTitle);
        CreateBookRequestDto bookRequestDto = new CreateBookRequestDto();
        bookRequestDto.setTitle(newTitle);
        BookDto expectedBookDto = new BookDto();
        expectedBookDto.setId(id);
        expectedBookDto.setTitle(newTitle);
        //when
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));
        doNothing().when(bookMapper).updateBookFromDto(book, bookRequestDto);
        when(bookRepository.save(book)).thenReturn(changedBook);
        when(bookMapper.toBookDto(changedBook)).thenReturn(expectedBookDto);
        BookDto result = bookService.update(id, bookRequestDto);
        //then
        Assertions.assertEquals(expectedBookDto, result);
    }

    @Test
    @DisplayName("delete should remove correct book")
    void delete_BookWithId1_True() {
        //given
        Long id = 1L;
        //when
        bookService.delete(id);
        //then
        verify(bookRepository).deleteById(id);
    }

    private List<Book> createListOf2Books() {
        Book firstBook = new Book();
        firstBook.setTitle("History");
        firstBook.setAuthor("Me");
        Book secondBook = new Book();
        secondBook.setTitle("Zoo");
        secondBook.setAuthor("Me");
        return List.of(firstBook, secondBook);
    }

    private List<BookDto> createListOf2BookDtos() {
        BookDto firstBookDto = new BookDto();
        firstBookDto.setTitle("History");
        firstBookDto.setAuthor("Me");
        BookDto secondBookDto = new BookDto();
        secondBookDto.setTitle("Zoo");
        secondBookDto.setAuthor("Me");
        return List.of(firstBookDto, secondBookDto);
    }
}
