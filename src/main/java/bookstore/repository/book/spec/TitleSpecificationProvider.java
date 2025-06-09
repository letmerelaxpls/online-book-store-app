package bookstore.repository.book.spec;

import bookstore.model.Book;
import bookstore.repository.SpecificationProvider;
import java.util.Arrays;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TitleSpecificationProvider implements SpecificationProvider<Book> {
    public static final String TITLE_KEY = "title";

    @Override
    public String getKey() {
        return TITLE_KEY;
    }

    public Specification<Book> getSpecification(String[] param) {
        return (root, query, criteriaBuilder)
                -> root.get(TITLE_KEY).in(Arrays.stream(param).toArray());
    }

}
