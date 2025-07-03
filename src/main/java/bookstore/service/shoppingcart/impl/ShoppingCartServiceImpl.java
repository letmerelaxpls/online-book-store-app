package bookstore.service.shoppingcart.impl;

import bookstore.dto.cartitem.CartItemRequestDto;
import bookstore.dto.cartitem.UpdateCartItemDto;
import bookstore.dto.shoppingcart.ShoppingCartResponseDto;
import bookstore.exception.EntityNotFoundException;
import bookstore.mapper.CartItemMapper;
import bookstore.mapper.ShoppingCartMapper;
import bookstore.model.CartItem;
import bookstore.model.ShoppingCart;
import bookstore.model.User;
import bookstore.repository.cartitem.CartItemRepository;
import bookstore.repository.shoppingcart.ShoppingCartRepository;
import bookstore.service.shoppingcart.ShoppingCartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;

    @Override
    public ShoppingCartResponseDto getCart(Long id) {
        return shoppingCartMapper.toResponseDto(
                shoppingCartRepository
                        .findById(id).orElseThrow(()
                                -> new EntityNotFoundException("Could not find ShoppingCart "
                                + "by id: " + id)));
    }

    @Override
    public ShoppingCartResponseDto addItem(Long id, CartItemRequestDto itemRequest) {
        ShoppingCart shoppingCart = shoppingCartRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Could not find ShoppingCart "
                        + "with id: " + id));
        CartItem cartItem = cartItemMapper.toModel(itemRequest);
        cartItem.setShoppingCart(shoppingCart);
        shoppingCart.getCartItems().add(cartItem);
        return shoppingCartMapper.toResponseDto(
                shoppingCartRepository.save(shoppingCart));
    }

    @Override
    public ShoppingCartResponseDto updateItem(Long userId,
                                          Long itemId,
                                          UpdateCartItemDto itemRequest) {
        ShoppingCart shoppingCart = shoppingCartRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Could not find ShoppingCart "
                + "by id: " + userId));
        CartItem cartItem = shoppingCart.getCartItems().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst().orElseThrow(()
                        -> new EntityNotFoundException("Could not find CartItem with id: "
                        + itemId + " and ShoppingCart User id: " + userId));

        cartItemMapper.updateCartItemFromDto(cartItem, itemRequest);
        return shoppingCartMapper.toResponseDto(shoppingCart);
    }

    @Override
    public void deleteItem(Long userId, Long itemId) {
        CartItem cartItem = cartItemRepository.findByIdAndShoppingCartUserId(itemId, userId)
                .orElseThrow(()
                        -> new EntityNotFoundException("Could not find CartItem with id: "
                        + itemId + " and User id: " + userId));
        cartItemRepository.delete(cartItem);
    }

    @Override
    public void createShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
    }
}
