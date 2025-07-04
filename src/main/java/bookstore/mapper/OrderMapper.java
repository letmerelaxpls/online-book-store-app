package bookstore.mapper;

import bookstore.config.MapperConfig;
import bookstore.dto.order.OrderResponseDto;
import bookstore.dto.order.UpdateOrderRequestDto;
import bookstore.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class, uses = OrderItemMapper.class)
public interface OrderMapper {
    @Mapping(target = "userId", source = "user.id")
    OrderResponseDto toResponseDto(Order order);

    void updateOrderStatus(@MappingTarget Order order,
                           UpdateOrderRequestDto updateOrderRequest);
}
