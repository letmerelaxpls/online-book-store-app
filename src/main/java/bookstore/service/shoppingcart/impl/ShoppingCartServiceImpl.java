package bookstore.service.shoppingcart.impl;

import bookstore.dto.cartitem.CartItemRequestDto;
import bookstore.dto.cartitem.CartItemRequestWithoutBookIdDto;
import bookstore.dto.cartitem.CartItemResponseDto;
import bookstore.dto.shoppingcart.ShoppingCartResponseDto;
import bookstore.exception.EntityNotFoundException;
import bookstore.mapper.CartItemMapper;
import bookstore.mapper.ShoppingCartMapper;
import bookstore.model.CartItem;
import bookstore.model.ShoppingCart;
import bookstore.repository.cartitem.CartItemRepository;
import bookstore.repository.shoppingcart.ShoppingCartRepository;
import bookstore.service.shoppingcart.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;

    @Override
    public ShoppingCartResponseDto getCart(String email) {
        return shoppingCartMapper.toResponseDto(
                shoppingCartRepository
                        .findByUserEmail(email).orElseThrow(()
                                -> new EntityNotFoundException("Could not find ShoppingCart "
                                + "by User email: " + email)));
    }

    @Override
    public void addItem(String email, CartItemRequestDto itemRequest) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Could not find "));
        CartItem cartItem = cartItemMapper.toModel(itemRequest);
        cartItem.setShoppingCart(shoppingCart);
        shoppingCart.getCartItems().add(cartItem);
        shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public CartItemResponseDto updateItem(String email,
                                          Long itemId,
                                          CartItemRequestWithoutBookIdDto itemRequest) {
        CartItem cartItem = cartItemRepository.findByIdAndShoppingCartUserEmail(itemId,
                        email)
                .orElseThrow(()
                        -> new EntityNotFoundException("Could not find CartItem with id: "
                        + itemId + " and User email: " + email));

        cartItemMapper.updateCartItemFromDto(cartItem, itemRequest);
        return cartItemMapper.toResponseDto(cartItemRepository.save(cartItem));
    }

    @Override
    public void deleteItem(String email, Long itemId) {
        CartItem cartItem = cartItemRepository.findByIdAndShoppingCartUserEmail(itemId, email)
                .orElseThrow(()
                        -> new EntityNotFoundException("Could not find CartItem with id: "
                        + itemId + " and User email: " + email));
        cartItemRepository.delete(cartItem);
    }
}
