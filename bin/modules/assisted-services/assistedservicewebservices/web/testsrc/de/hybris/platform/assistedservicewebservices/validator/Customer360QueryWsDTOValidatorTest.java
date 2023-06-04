/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.assistedservicewebservices.validator;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.assistedservicewebservices.dto.Customer360QueryWsDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * Test suite for {@link Customer360QueryWsDTOValidator}
 */
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class Customer360QueryWsDTOValidatorTest {
    private static final String OBJECT_NAME = "customer360QueryWsDTO";
    private static final String FIELD_REQUIRED_ERROR_CODE = "field.required";
    private static final String FIELD_INVALID_ERROR_CODE = "field.invalid";
    private static final String QUERY_TYPE = "C360Type";
    private List<String> validTypes;

    private Customer360QueryWsDTOValidator customer360QueryWsDTOValidator;

    @Before
    public void setUp() {
        customer360QueryWsDTOValidator = new Customer360QueryWsDTOValidator();
        validTypes = Arrays.asList(QUERY_TYPE);
        customer360QueryWsDTOValidator.setValidTypes(validTypes);
    }

    @Test
    public void testValidateWhenTypeIsNull() {
        Customer360QueryWsDTO customer360QueryWsDTO = new Customer360QueryWsDTO();
        final Errors errors = new BeanPropertyBindingResult(customer360QueryWsDTO, OBJECT_NAME);

        customer360QueryWsDTOValidator.validate(customer360QueryWsDTO, errors);

        assertThat(errors.getErrorCount()).isEqualTo(1);
        assertThat(errors.getFieldError().getField()).isEqualTo("type");
        assertThat(errors.getFieldError().getCode()).isEqualTo(FIELD_REQUIRED_ERROR_CODE);
    }

    @Test
    public void testValidateWhenTypeIsEmpty() {
        Customer360QueryWsDTO customer360QueryWsDTO = new Customer360QueryWsDTO();
        customer360QueryWsDTO.setType("");
        final Errors errors = new BeanPropertyBindingResult(customer360QueryWsDTO, OBJECT_NAME);

        customer360QueryWsDTOValidator.validate(customer360QueryWsDTO, errors);

        assertThat(errors.getErrorCount()).isEqualTo(1);
        assertThat(errors.getFieldError().getField()).isEqualTo("type");
        assertThat(errors.getFieldError().getCode()).isEqualTo(FIELD_REQUIRED_ERROR_CODE);
    }

    @Test
    public void testValidateWhenTypeIsInvalid() {
        Customer360QueryWsDTO customer360QueryWsDTO = new Customer360QueryWsDTO();
        customer360QueryWsDTO.setType("invalidType");
        final Errors errors = new BeanPropertyBindingResult(customer360QueryWsDTO, OBJECT_NAME);

        customer360QueryWsDTOValidator.validate(customer360QueryWsDTO, errors);

        assertThat(errors.getErrorCount()).isEqualTo(1);
        assertThat(errors.getFieldError().getField()).isEqualTo("type");
        assertThat(errors.getFieldError().getCode()).isEqualTo(FIELD_INVALID_ERROR_CODE);
    }

    @Test
    public void testValidateWhenTypeIsValid() {
        Customer360QueryWsDTO customer360QueryWsDTO = new Customer360QueryWsDTO();
        customer360QueryWsDTO.setType(QUERY_TYPE);
        final Errors errors = new BeanPropertyBindingResult(customer360QueryWsDTO, OBJECT_NAME);

        customer360QueryWsDTOValidator.validate(customer360QueryWsDTO, errors);

        assertThat(errors.getErrorCount()).isZero();
    }
}
