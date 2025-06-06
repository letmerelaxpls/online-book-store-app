package bookstore.repository.book;

import bookstore.exception.SpecificationProviderManagerException;
import bookstore.model.Book;
import bookstore.repository.SpecificationProvider;
import bookstore.repository.SpecificationProviderManager;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookSpecificationProviderManager implements SpecificationProviderManager<Book> {
    private final List<SpecificationProvider<Book>> bookSpecificationProviders;

    @Override
    public SpecificationProvider<Book> getSpecificationProvider(String key) {
        return bookSpecificationProviders.stream()
                .filter(p -> p.getKey().equals(key))
                .findFirst()
                .orElseThrow(()
                        -> new SpecificationProviderManagerException("Could not find "
                        + "correct specification for key: " + key));
    }
}
