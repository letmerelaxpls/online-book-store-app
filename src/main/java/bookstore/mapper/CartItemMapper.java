package bookstore.mapper;

import bookstore.config.MapperConfig;
import bookstore.dto.cartitem.CartItemRequestDto;
import bookstore.dto.cartitem.CartItemResponseDto;
import bookstore.dto.cartitem.UpdateCartItemDto;
import bookstore.model.Book;
import bookstore.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

@Mapper(config = MapperConfig.class)
public interface CartItemMapper {
    @Mapping(target = "shoppingCart", ignore = true)
    @Mapping(target = "book", source = "bookId", qualifiedByName = "bookById")
    CartItem toModel(CartItemRequestDto requestItem);

    @Mapping(target = "bookId", source = "book.id")
    @Mapping(target = "bookTitle", source = "book.title")
    CartItemResponseDto toResponseDto(CartItem cartItem);

    void updateCartItemFromDto(@MappingTarget CartItem cartItem,
                               UpdateCartItemDto requestItem);

    @Named("bookById")
    default Book bookById(Long id) {
        if (id == null) {
            return null;
        }
        Book book = new Book();
        book.setId(id);
        return book;
    }
}
