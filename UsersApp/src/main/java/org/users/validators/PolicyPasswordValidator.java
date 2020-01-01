package org.users.validators;

import java.util.regex.Pattern;

import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;

public class PolicyPasswordValidator implements IValidator<String> {
    
    private static final long serialVersionUID = 1L;
    private static final Pattern UPPER = Pattern.compile("[A-Z]");
    private static final Pattern LOWER = Pattern.compile("[a-z]");
    private static final Pattern NUMBER = Pattern.compile("[0-9]");
    
	@Override
	public void validate(IValidatable<String> validatable) {
        final String password = validatable.getValue();
        if (!NUMBER.matcher(password).find())
            error(validatable, "NO DIGITS");
        if (!UPPER.matcher(password).find())
            error(validatable, "YOU NEED AT LEAST 1 UPPER CASE LETTER");
        if (!LOWER.matcher(password).find())
            error(validatable, "YOU NEED AT LEAST ONE LOWERCASE LETTER");
    }
    
    private void error(IValidatable<String> validatable, String message) {
        validatable.error(new ValidationError().setMessage(message));
    }
}