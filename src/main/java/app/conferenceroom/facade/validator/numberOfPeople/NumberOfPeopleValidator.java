package app.conferenceroom.facade.validator.numberOfPeople;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

@Slf4j
public class NumberOfPeopleValidator implements ConstraintValidator<ValidNumberOfPeople, Integer> {

    @Value("${app.booking.min-people}")
    private int minPeople;

    @Value("${app.booking.max-people}")
    private int maxPeople;

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null) {
            return addErrorMessage(context, "Number of people cannot be blank");
        }
        return validateNumberOfPeople(context, value);
    }

    private boolean addErrorMessage(ConstraintValidatorContext context, String message) {
        log.info("NumberOfPeopleValidator Error message: {}", message);
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
        return false;
    }

    private boolean validateNumberOfPeople(ConstraintValidatorContext context, int value) {
        if (value < minPeople) {
            return addErrorMessage(context, String.format("Minimum %d people are required", minPeople));
        } else if (value > maxPeople) {
            return addErrorMessage(context, String.format("Maximum %d people are allowed", maxPeople));
        }
        return true;
    }
}
