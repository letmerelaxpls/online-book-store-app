package bookstore.service.order;

import bookstore.dto.order.OrderRequestDto;
import bookstore.dto.order.OrderResponseDto;
import bookstore.dto.order.UpdateOrderRequestDto;
import bookstore.dto.orderitem.OrderItemResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    Page<OrderResponseDto> getAllOrders(Long userId,
                                        Pageable pageable);

    OrderResponseDto createOrder(Long userId, OrderRequestDto orderRequest);

    OrderResponseDto updateOrder(Long orderId,
                                 UpdateOrderRequestDto updateOrderRequest);

    Page<OrderItemResponseDto> getAllOrderItems(Long userId,
                                                Long orderId,
                                                Pageable pageable);

    OrderItemResponseDto getOrderItem(Long userId,
                                      Long orderId,
                                      Long itemId);
}
