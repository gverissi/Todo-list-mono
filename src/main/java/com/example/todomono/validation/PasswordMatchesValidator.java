package com.example.todomono.validation;

import com.example.todomono.form.ChangeCustomerPasswordForm;
import com.example.todomono.form.CustomerForm;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext context) {
        boolean validity = false;
        if (o instanceof CustomerForm) {
            CustomerForm customerForm = (CustomerForm) o;
            validity = customerForm.getPassword().equals(customerForm.getMatchingPassword());
        } else if (o instanceof ChangeCustomerPasswordForm) {
            ChangeCustomerPasswordForm changeCustomerPasswordForm = (ChangeCustomerPasswordForm) o;
            validity = changeCustomerPasswordForm.getPassword().equals(changeCustomerPasswordForm.getMatchingPassword());
        }
        if(!validity){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode( "matchingPassword" ).addConstraintViolation();
        }
        return validity;
    }

}
