package bookstore.repository;

import bookstore.dto.BookSearchParametersDto;
import bookstore.model.Book;
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
                            .getSpecificationProvider("title")
                            .getSpecification(searchParametersDto.titles()));
        }
        if (searchParametersDto.authors() != null && searchParametersDto.authors().length > 0) {
            specification = specification
                    .and(bookSpecificationProviderManager
                            .getSpecificationProvider("author")
                            .getSpecification(searchParametersDto.authors()));
        }
        if (searchParametersDto.isbns() != null && searchParametersDto.isbns().length > 0) {
            specification = specification
                    .and(bookSpecificationProviderManager
                            .getSpecificationProvider("isbn")
                            .getSpecification(searchParametersDto.isbns()));
        }
        return specification;
    }
}
