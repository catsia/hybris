/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.assistedservicewebservices.validator;

import de.hybris.platform.assistedservicewebservices.dto.Customer360QueryWsDTO;
import org.apache.solr.common.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.List;

public class Customer360QueryWsDTOValidator implements Validator {
    protected static final String FIELD_REQUIRED = "field.required";
    protected static final String FIELD_INVALID = "field.invalid";
    protected static final String TYPE = "type";

    private List<String> validTypes;

    @Override
    public boolean supports(Class<?> clazz) {
        return Customer360QueryWsDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Customer360QueryWsDTO customer360QueryWsDTO = (Customer360QueryWsDTO) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, TYPE, FIELD_REQUIRED);

        if (!StringUtils.isEmpty(customer360QueryWsDTO.getType()) && !validTypes.contains(customer360QueryWsDTO.getType()))
        {
            errors.rejectValue(TYPE, FIELD_INVALID, new String[] { TYPE }, "Invalid field: {0}");
        }
    }

    public List<String> getValidTypes()
    {
        return validTypes;
    }

    public void setValidTypes(List<String> validTypes)
    {
        this.validTypes = validTypes;
    }
}
