package bookstore.validation;

import bookstore.dto.user.UserRegistrationRequestDto;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchValidator
        implements ConstraintValidator<PasswordMatch, UserRegistrationRequestDto> {
    @Override
    public boolean isValid(
            UserRegistrationRequestDto userRegistrationRequestDto,
            ConstraintValidatorContext constraintValidatorContext) {
        if (userRegistrationRequestDto.getPassword() == null
                || userRegistrationRequestDto.getRepeatPassword() == null) {
            return false;
        }
        return userRegistrationRequestDto.getPassword()
                .equals(userRegistrationRequestDto.getRepeatPassword());
    }
}
