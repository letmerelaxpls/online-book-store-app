package bookstore.mapper;

import bookstore.config.MapperConfig;
import bookstore.dto.book.BookDto;
import bookstore.dto.book.BookDtoWithoutCategoryIds;
import bookstore.dto.book.CreateBookRequestDto;
import bookstore.model.Book;
import bookstore.model.Category;
import java.util.stream.Collectors;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    @Mapping(target = "categories", ignore = true)
    Book toBook(CreateBookRequestDto bookDto);

    @AfterMapping
    default void setCategories(@MappingTarget Book book, CreateBookRequestDto bookDto) {
        book.setCategories(
                bookDto.getCategoryIds().stream()
                        .map(Category::new)
                        .collect(Collectors.toSet())
        );
    }

    @Mapping(target = "categoryIds", ignore = true)
    BookDto toBookDto(Book book);

    @AfterMapping
    default void setCategoryIds(@MappingTarget BookDto bookDto, Book book) {
        bookDto.setCategoryIds(
                book.getCategories()
                        .stream()
                        .map(Category::getId)
                        .toList()
        );
    }

    BookDtoWithoutCategoryIds toDtoWithoutCategories(Book book);

    @Mapping(target = "categories", ignore = true)
    void updateBookFromDto(@MappingTarget Book book, CreateBookRequestDto bookDto);
}
