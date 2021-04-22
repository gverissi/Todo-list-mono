package com.example.todomono.validation;

import com.example.todomono.form.CustomerChangePasswordForm;
import com.example.todomono.form.CustomerCreateForm;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(PasswordMatches constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext context) {
        boolean validity = false;
        if (o instanceof CustomerCreateForm) {
            CustomerCreateForm customerCreateForm = (CustomerCreateForm) o;
            validity = customerCreateForm.getPassword().equals(customerCreateForm.getMatchingPassword());
        } else if (o instanceof CustomerChangePasswordForm) {
            CustomerChangePasswordForm customerChangePasswordForm = (CustomerChangePasswordForm) o;
            validity = customerChangePasswordForm.getPassword().equals(customerChangePasswordForm.getMatchingPassword());
        }
        if(!validity){
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode( "matchingPassword" ).addConstraintViolation();
        }
        return validity;
    }

}
