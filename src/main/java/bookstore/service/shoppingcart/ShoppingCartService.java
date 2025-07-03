package bookstore.service.shoppingcart;

import bookstore.dto.cartitem.CartItemRequestDto;
import bookstore.dto.cartitem.UpdateCartItemDto;
import bookstore.dto.shoppingcart.ShoppingCartResponseDto;
import bookstore.model.User;

public interface ShoppingCartService {
    ShoppingCartResponseDto getCart(Long id);

    ShoppingCartResponseDto addItem(Long id, CartItemRequestDto itemRequest);

    ShoppingCartResponseDto updateItem(Long userId,
                                   Long itemId,
                                   UpdateCartItemDto itemRequest);

    void deleteItem(Long userId, Long itemId);

    void createShoppingCart(User user);
}
