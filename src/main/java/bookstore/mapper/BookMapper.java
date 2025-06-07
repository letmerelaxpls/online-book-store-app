package bookstore.mapper;

import bookstore.config.MapperConfig;
import bookstore.dto.BookDto;
import bookstore.dto.CreateBookRequestDto;
import bookstore.model.Book;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface BookMapper {
    Book toBook(CreateBookRequestDto bookDto);

    BookDto toBookDto(Book book);

    void updateBookFromDto(@MappingTarget Book book, CreateBookRequestDto bookDto);
}
