package bookstore.controller;

import bookstore.dto.order.OrderRequestDto;
import bookstore.dto.order.OrderResponseDto;
import bookstore.dto.order.UpdateOrderRequestDto;
import bookstore.dto.orderitem.OrderItemResponseDto;
import bookstore.service.order.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Orders management",
        description = "Endpoints for managing orders and order items")
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @Operation(summary = "Get orders history",
            description = "Endpoint for retrieving orders history for a specific user")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public Page<OrderResponseDto> getAllOrders(Authentication authentication,
                                               Pageable pageable) {
        Long userId = (Long) authentication.getPrincipal();
        return orderService.getAllOrders(userId, pageable);
    }

    @Operation(summary = "Create an order",
            description = "Endpoint for creating a new order for a specific user")
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public OrderResponseDto createOrder(Authentication authentication,
                                        @RequestBody @Valid OrderRequestDto orderRequest) {
        Long userId = (Long) authentication.getPrincipal();
        return orderService.createOrder(userId, orderRequest);
    }

    @Operation(summary = "Update an order",
            description = "Endpoint for updating order`s status for a specific user")
    @PatchMapping("/{orderId}")
    @PreAuthorize("hasRole('ADMIN')")
    public OrderResponseDto updateOrder(
            @PathVariable Long orderId,
            @RequestBody @Valid UpdateOrderRequestDto updateOrderRequest) {
        return orderService.updateOrder(orderId, updateOrderRequest);
    }

    @Operation(summary = "Get order items",
            description = "Endpoint for retrieving all order items "
                    + "in a specific order for a specific user")
    @GetMapping("/{orderId}/items")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public Page<OrderItemResponseDto> getAllOrderItems(
            Authentication authentication,
            @PathVariable Long orderId,
            Pageable pageable) {
        Long userId = (Long) authentication.getPrincipal();
        return orderService.getAllOrderItems(userId, orderId, pageable);
    }

    @Operation(summary = "Get an order item",
            description = "Endpoint for getting a specific order item "
                    + "in a specific order for a specific user")
    @GetMapping("/{orderId}/items/{itemId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public OrderItemResponseDto getOrderItem(
            Authentication authentication,
            @PathVariable Long orderId,
            @PathVariable Long itemId) {
        Long userId = (Long) authentication.getPrincipal();
        return orderService.getOrderItem(userId, orderId, itemId);
    }
}
