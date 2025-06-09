package bookstore.repository.book;

import static bookstore.repository.book.spec.AuthorSpecificationProvider.AUTHOR_KEY;
import static bookstore.repository.book.spec.IsbnSpecificationProvider.ISBN_KEY;
import static bookstore.repository.book.spec.TitleSpecificationProvider.TITLE_KEY;

import bookstore.dto.BookSearchParametersDto;
import bookstore.model.Book;
import bookstore.repository.SpecificationBuilder;
import bookstore.repository.SpecificationProviderManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookSpecificationBuilder implements SpecificationBuilder<Book> {
    private final SpecificationProviderManager<Book> bookSpecificationProviderManager;

    @Override
    public Specification<Book> build(BookSearchParametersDto searchParametersDto) {
        Specification<Book> specification = Specification.where(null);
        if (searchParametersDto.titles() != null && searchParametersDto.titles().length > 0) {
            specification = specification
                    .and(bookSpecificationProviderManager
                            .getSpecificationProvider(TITLE_KEY)
                            .getSpecification(searchParametersDto.titles()));
        }
        if (searchParametersDto.authors() != null && searchParametersDto.authors().length > 0) {
            specification = specification
                    .and(bookSpecificationProviderManager
                            .getSpecificationProvider(AUTHOR_KEY)
                            .getSpecification(searchParametersDto.authors()));
        }
        if (searchParametersDto.isbns() != null && searchParametersDto.isbns().length > 0) {
            specification = specification
                    .and(bookSpecificationProviderManager
                            .getSpecificationProvider(ISBN_KEY)
                            .getSpecification(searchParametersDto.isbns()));
        }
        return specification;
    }
}
