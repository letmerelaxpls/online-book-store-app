package bookstore.service.order.impl;

import bookstore.dto.order.OrderRequestDto;
import bookstore.dto.order.OrderResponseDto;
import bookstore.dto.order.UpdateOrderRequestDto;
import bookstore.dto.orderitem.OrderItemResponseDto;
import bookstore.exception.EntityNotFoundException;
import bookstore.exception.OrderProcessingException;
import bookstore.mapper.OrderItemMapper;
import bookstore.mapper.OrderMapper;
import bookstore.model.CartItem;
import bookstore.model.Order;
import bookstore.model.OrderItem;
import bookstore.model.ShoppingCart;
import bookstore.model.User;
import bookstore.repository.order.OrderRepository;
import bookstore.repository.orderitem.OrderItemRepository;
import bookstore.repository.shoppingcart.ShoppingCartRepository;
import bookstore.service.order.OrderService;
import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderMapper orderMapper;
    private final OrderRepository orderRepository;
    private final OrderItemMapper orderItemMapper;
    private final OrderItemRepository orderItemRepository;
    private final ShoppingCartRepository shoppingCartRepository;

    @Override
    public Page<OrderResponseDto> getAllOrders(Long userId,
                                               Pageable pageable) {
        return orderRepository.findAllByUserId(userId, pageable)
                .map(orderMapper::toResponseDto);
    }

    @Override
    public OrderResponseDto createOrder(Long userId, OrderRequestDto orderRequest) {
        ShoppingCart shoppingCart = shoppingCartRepository.findWithItemsAndBooksById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Could not find ShoppingCart "
                        + "with id: " + userId));
        if (shoppingCart.getCartItems().isEmpty()) {
            throw new OrderProcessingException("Could not process the Order "
                    + "because ShoppingCart is empty!");
        }
        User user = shoppingCart.getUser();
        Order order = initializeOrder(user, shoppingCart, orderRequest);
        shoppingCart.getCartItems().clear();
        return orderMapper.toResponseDto(orderRepository.save(order));
    }

    @Override
    public OrderResponseDto updateOrder(Long orderId, UpdateOrderRequestDto updateOrderRequest) {
        Order order = orderRepository.findWithUserAndItemsById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Could not find Order with id: "
                        + orderId));
        orderMapper.updateOrderStatus(order, updateOrderRequest);
        return orderMapper.toResponseDto(order);
    }

    @Override
    public Page<OrderItemResponseDto> getAllOrderItems(Long userId,
                                                       Long orderId,
                                                       Pageable pageable) {
        return orderItemRepository.findAllByOrderIdAndOrderUserId(
                orderId, userId, pageable)
                .map(orderItemMapper::toResponseDto);
    }

    @Override
    public OrderItemResponseDto getOrderItem(Long userId, Long orderId, Long itemId) {
        OrderItem orderItem = orderItemRepository.findByIdAndOrderIdAndOrderUserId(
                itemId, orderId, userId).orElseThrow(()
                        -> new EntityNotFoundException("Could not find OrderItem with id: "
                + itemId + ", Order id: " + orderId + " and User id: " + userId));

        return orderItemMapper.toResponseDto(orderItem);
    }

    private Order initializeOrder(User user,
                                  ShoppingCart shoppingCart,
                                  OrderRequestDto orderRequest) {
        Order order = new Order();
        order.setUser(user);
        order.setOrderItems(toOrderItems(shoppingCart.getCartItems(), order));
        order.setTotal(calculateTotal(order.getOrderItems()));
        order.setShippingAddress(orderRequest.getShippingAddress());
        return order;
    }

    private Set<OrderItem> toOrderItems(Set<CartItem> cartItems, Order order) {
        return cartItems.stream()
                .map(ci -> {
                    OrderItem orderItem = orderItemMapper.toModelFromCartItem(ci);
                    orderItem.setOrder(order);
                    return orderItem;
                })
                .collect(Collectors.toSet());
    }

    private BigDecimal calculateTotal(Set<OrderItem> orderItems) {
        return orderItems.stream()
                .map(OrderItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
