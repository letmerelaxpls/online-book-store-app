package bookstore.repository.book;

import bookstore.model.Book;
import bookstore.repository.SpecificationProvider;
import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class AuthorSpecificationProvider implements SpecificationProvider<Book> {
    @Override
    public String getKey() {
        return "author";
    }

    @Override
    public Specification<Book> getSpecification(String[] param) {
        return (root, query, criteriaBuilder)
                -> root.get("author").in(Arrays.stream(param).toArray());
    }
}
