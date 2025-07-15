package bookstore.util;

import bookstore.dto.book.BookDto;
import bookstore.dto.book.CreateBookRequestDto;
import bookstore.dto.category.CategoryRequestDto;
import bookstore.dto.category.CategoryResponseDto;
import bookstore.model.Book;
import bookstore.model.Category;
import java.math.BigDecimal;
import java.util.List;

public class TestUtil {
    public static Category createHorrorCategory() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Horror");
        return category;
    }

    public static CategoryResponseDto createHorrorCategoryResponseDto() {
        CategoryResponseDto categoryResponseDto = new CategoryResponseDto();
        categoryResponseDto.setId(1L);
        categoryResponseDto.setName("Horror");
        return categoryResponseDto;
    }

    public static CategoryRequestDto createHorrorCategoryRequestDto() {
        CategoryRequestDto categoryRequestDto = new CategoryRequestDto();
        categoryRequestDto.setName("Horror");
        return categoryRequestDto;
    }

    public static List<Book> createListOfTwoBooks() {
        Book firstBook = new Book();
        firstBook.setTitle("History");
        firstBook.setAuthor("Me");
        Book secondBook = new Book();
        secondBook.setTitle("Zoo");
        secondBook.setAuthor("Me");
        return List.of(firstBook, secondBook);
    }

    public static List<BookDto> createListOfTwoBookDtos() {
        BookDto firstBookDto = new BookDto();
        firstBookDto.setTitle("History");
        firstBookDto.setAuthor("Me");
        BookDto secondBookDto = new BookDto();
        secondBookDto.setTitle("Zoo");
        secondBookDto.setAuthor("Me");
        return List.of(firstBookDto, secondBookDto);
    }

    public static CreateBookRequestDto createBookWithTitleThat() {
        CreateBookRequestDto book = new CreateBookRequestDto();
        book.setTitle("That");
        book.setAuthor("Stephen King");
        book.setIsbn("9780307743658");
        book.setPrice(BigDecimal.valueOf(19.99));
        book.setDescription("Classic horror novel");
        book.setCategoryIds(List.of(4L));
        return book;
    }

    public static BookDto createBookDtoWithTitleThat() {
        BookDto book = new BookDto();
        book.setTitle("That");
        book.setAuthor("Stephen King");
        book.setIsbn("9780307743658");
        book.setPrice(BigDecimal.valueOf(19.99));
        book.setDescription("Classic horror novel");
        book.setCategoryIds(List.of(4L));
        return book;
    }
}
