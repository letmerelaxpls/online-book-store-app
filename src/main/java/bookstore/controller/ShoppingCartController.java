package bookstore.controller;

import bookstore.dto.cartitem.CartItemRequestDto;
import bookstore.dto.cartitem.UpdateCartItemDto;
import bookstore.dto.shoppingcart.ShoppingCartResponseDto;
import bookstore.service.shoppingcart.ShoppingCartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Shopping cart management",
        description = "Endpoints for managing users` shopping carts")
@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @Operation(summary = "Get user shopping cart",
            description = "Endpoint to get user`s shopping cart with cartItems")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ShoppingCartResponseDto getCart(Authentication authentication) {
        Long userId = (Long) authentication.getPrincipal();
        return shoppingCartService.getCart(userId);
    }

    @Operation(summary = "Add cart item",
            description = "Endpoint for adding cart items in user`s shopping cart")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ShoppingCartResponseDto addItem(Authentication authentication,
                        @RequestBody @Valid CartItemRequestDto requestItem) {
        Long userId = (Long) authentication.getPrincipal();
        return shoppingCartService.addItem(userId, requestItem);
    }

    @Operation(summary = "Update cart item quantity",
            description = "Endpoint for updating cart item`s quantity")
    @PutMapping("/items/{cartItemId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ShoppingCartResponseDto updateItem(
            Authentication authentication,
            @PathVariable Long cartItemId,
            @RequestBody @Valid UpdateCartItemDto requestItem) {
        Long userId = (Long) authentication.getPrincipal();
        return shoppingCartService.updateItem(userId, cartItemId, requestItem);
    }

    @Operation(summary = "Delete item from shopping cart",
            description = "Endpoint for deleting cart item from user`s shopping cart")
    @DeleteMapping("/items/{cartItemId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public void deleteItem(Authentication authentication,
                           @PathVariable Long cartItemId) {
        Long userId = (Long) authentication.getPrincipal();
        shoppingCartService.deleteItem(userId, cartItemId);
    }
}
