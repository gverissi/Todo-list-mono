package com.example.todomono.validation;

import com.example.todomono.form.CustomerForm;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext context) {
        CustomerForm customerForm = (CustomerForm) o;
        boolean validity = customerForm.getPassword().equals(customerForm.getMatchingPassword());
        if(!validity){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode( "matchingPassword" ).addConstraintViolation();
        }
        return validity;
    }

}
