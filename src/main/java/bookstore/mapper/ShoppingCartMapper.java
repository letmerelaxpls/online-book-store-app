package bookstore.mapper;

import bookstore.config.MapperConfig;
import bookstore.dto.shoppingcart.ShoppingCartResponseDto;
import bookstore.model.ShoppingCart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class, uses = CartItemMapper.class)
public interface ShoppingCartMapper {
    @Mapping(target = "userId", source = "user.id")
    ShoppingCartResponseDto toResponseDto(ShoppingCart shoppingCart);
}
