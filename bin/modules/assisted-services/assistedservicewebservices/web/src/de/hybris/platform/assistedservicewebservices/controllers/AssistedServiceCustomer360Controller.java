/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.assistedservicewebservices.controllers;

import de.hybris.platform.assistedservicefacades.customer360.AdditionalInformationFrameworkFacade;
import de.hybris.platform.assistedservicewebservices.dto.Customer360QueryListWsDTO;
import de.hybris.platform.assistedservicewebservices.dto.Customer360DataWsDTO;
import de.hybris.platform.assistedservicewebservices.dto.Customer360ListWsDTO;
import de.hybris.platform.webservicescommons.errors.exceptions.WebserviceValidationException;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
import de.hybris.platform.webservicescommons.swagger.ApiBaseSiteIdAndUserIdParam;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@Tag(name = "ASM Customer 360")
public class AssistedServiceCustomer360Controller {
    @Resource(name = "additionalInformationFrameworkFacade")
    private AdditionalInformationFrameworkFacade additionalInformationFrameworkFacade;
    @Resource(name = "customer360DataTypeSectionMap")
    private Map<String, String> dataTypeSectionMap;
    @Resource(name = "customer360DataTypeFragmentMap")
    private Map<String, String> dataTypeFragmentMap;
    @Resource
    private Validator customer360QueryListValidator;
    @Resource(name = "dataMapper")
    private DataMapper dataMapper;

    @PostMapping(value="/{baseSiteId}/users/{userId}/customer360", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Operation(operationId = "searchCustomer360", summary = "Retrieves the Customer 360 data.", description = "Retrieves a list of Customer 360 data.")
    @ApiBaseSiteIdAndUserIdParam
    @ResponseBody
    public Customer360ListWsDTO searchCustomer360(
            @Parameter(description = "Base site ID", required = true) @PathVariable(required = true) final String baseSiteId,
            @Parameter(description = "User identifier", required = true) @PathVariable(required = true) final String userId,
            @Parameter(description = "The list of Customer 360 Data to be queried", required = true) @RequestBody @Nonnull final Customer360QueryListWsDTO customer360QueryList
    ) {
        validate(customer360QueryList, "customer360QueryList", getCustomer360QueryListValidator());

        final List<Customer360DataWsDTO> customer360Data = customer360QueryList.getCustomer360Queries()
                .stream()
                .map(
                        query -> {
                            final String type = query.getType();
                            return additionalInformationFrameworkFacade.getFragment(
                                    getDataTypeSectionMap().get(type),
                                    getDataTypeFragmentMap().get(type),
                                    query.getAdditionalRequestParameters()
                            );
                        }
                )
                .map(fragment -> getDataMapper().map(fragment.getData(), Customer360DataWsDTO.class))
                .collect(Collectors.toList());

        final Customer360ListWsDTO customer360List = new Customer360ListWsDTO();
        customer360List.setValue(customer360Data);

        return customer360List;
    }

    protected void validate(final Object object, final String objectName, final Validator validator)
    {
        final Errors errors = new BeanPropertyBindingResult(object, objectName);
        validator.validate(object, errors);
        if (errors.hasErrors())
        {
            throw new WebserviceValidationException(errors);
        }
    }

    public Map<String, String> getDataTypeSectionMap() { return dataTypeSectionMap; }

    public Map<String, String> getDataTypeFragmentMap()
    {
        return dataTypeFragmentMap;
    }

    public Validator getCustomer360QueryListValidator() {
        return customer360QueryListValidator;
    }

    public void setCustomer360QueryListValidator(Validator customer360QueryListValidator)
    {
        this.customer360QueryListValidator = customer360QueryListValidator;
    }

    public DataMapper getDataMapper() { return dataMapper; }

    public void setDataMapper(DataMapper dataMapper)
    {
        this.dataMapper = dataMapper;
    }

    public AdditionalInformationFrameworkFacade getAdditionalInformationFrameworkFacade()
    {
        return additionalInformationFrameworkFacade;
    }

    public void setAdditionalInformationFrameworkFacade(AdditionalInformationFrameworkFacade additionalInformationFrameworkFacade)
    {
        this.additionalInformationFrameworkFacade = additionalInformationFrameworkFacade;
    }

    public void setDataTypeSectionMap(Map<String, String> dataTypeSectionMap)
    {
        this.dataTypeSectionMap = dataTypeSectionMap;
    }

    public void setDataTypeFragmentMap(Map<String, String> dataTypeFragmentMap)
    {
        this.dataTypeFragmentMap = dataTypeFragmentMap;
    }
}
