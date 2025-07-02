package bookstore.service.shoppingcart;

import bookstore.dto.cartitem.CartItemRequestDto;
import bookstore.dto.cartitem.CartItemRequestWithoutBookIdDto;
import bookstore.dto.cartitem.CartItemResponseDto;
import bookstore.dto.shoppingcart.ShoppingCartResponseDto;

public interface ShoppingCartService {
    ShoppingCartResponseDto getCart(String email);

    void addItem(String email, CartItemRequestDto itemRequest);

    CartItemResponseDto updateItem(String email,
                                   Long itemId,
                                   CartItemRequestWithoutBookIdDto itemRequest);

    void deleteItem(String email, Long itemId);
}
