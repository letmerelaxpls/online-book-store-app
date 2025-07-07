package bookstore.repository.shoppingcart;

import bookstore.model.ShoppingCart;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    @EntityGraph(attributePaths = {"cartItems", "cartItems.book"})
    Optional<ShoppingCart> findWithItemsAndBooksById(Long id);

    @EntityGraph(attributePaths = "user")
    Optional<ShoppingCart> findWithUserById(Long id);
}
