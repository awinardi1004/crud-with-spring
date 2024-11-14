package com.example.demo.util;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

public class Util {
    public static String getValidationErrors(BindingResult bindingResult) {
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        String validationErrors = fieldErrors.get(0).getDefaultMessage();

        if (fieldErrors.size() > 1) {
            for (int i = 1; i < fieldErrors.size(); i++) {
                validationErrors += ", " + fieldErrors.get(i).getDefaultMessage();
            }
        }

        return validationErrors;
    }
}
