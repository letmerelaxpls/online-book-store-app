package bookstore.repository.book.spec;

import bookstore.model.Book;
import bookstore.repository.SpecificationProvider;
import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class IsbnSpecificationProvider implements SpecificationProvider<Book> {
    public static final String ISBN_KEY = "isbn";

    @Override
    public String getKey() {
        return ISBN_KEY;
    }

    public Specification<Book> getSpecification(String[] params) {
        return (root, query, criteriaBuilder)
                -> root.get(ISBN_KEY).in(Arrays.stream(params).toArray());
    }
}
