package bookstore.dto.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class OrderRequestDto {
    @NotBlank
    @Pattern(regexp = "^[A-Z][a-z]+( [a-zA-Z]+)*, [A-Z][a-z]+( [a-zA-Z]+)*, \\d+[a-zA-Z]?$")
    private String shippingAddress;
}
