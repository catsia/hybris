package de.hybris.platform.assistedservicewebservices.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import de.hybris.bootstrap.annotations.UnitTest;
import de.hybris.platform.assistedservicefacades.customer360.AdditionalInformationFrameworkFacade;
import de.hybris.platform.assistedservicefacades.customer360.Fragment;
import de.hybris.platform.assistedservicefacades.customer360.StoreLocationData;
import de.hybris.platform.assistedservicewebservices.dto.Customer360QueryListWsDTO;
import de.hybris.platform.assistedservicewebservices.dto.Customer360QueryWsDTO;
import de.hybris.platform.assistedservicewebservices.dto.C360StoreLocation;
import de.hybris.platform.assistedservicewebservices.dto.Customer360DataWsDTO;
import de.hybris.platform.assistedservicewebservices.dto.Customer360ListWsDTO;
import de.hybris.platform.webservicescommons.errors.exceptions.WebserviceValidationException;
import de.hybris.platform.webservicescommons.mapping.DataMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Test suite for {@link AssistedServiceCustomer360Controller}
 */
@UnitTest
@RunWith(MockitoJUnitRunner.class)
public class AssistedServiceCustomer360ControllerTest {
    private static final String QUERY_TYPE = "C360Type";
    private static final String BASE_SITE = "baseSiteID";
    private static final String USER_ID = "userID";
    private static final String FRAGMENT_ID = "fragmentID";
    private static final String SECTION_ID = "sectionID";
    private static final String STORE_LOCATION_ADDRESS = "address";
    private static final String STORE_LOCATION_TYPE = "c360StoreLocation";

    @Mock
    private AdditionalInformationFrameworkFacade additionalInformationFrameworkFacade;
    @Mock
    private Validator customer360QueryListValidator;
    @Mock
    private DataMapper dataMapper;

    private Map<String, String> dataTypeSectionMap;
    private Map<String, String> dataTypeFragmentMap;

    private AssistedServiceCustomer360Controller assistedServiceCustomer360Controller;

    @Before
    public void setUp() {
        assistedServiceCustomer360Controller = new AssistedServiceCustomer360Controller();
        assistedServiceCustomer360Controller.setDataMapper(dataMapper);
        assistedServiceCustomer360Controller
                .setCustomer360QueryListValidator(customer360QueryListValidator);
        assistedServiceCustomer360Controller
                .setAdditionalInformationFrameworkFacade(additionalInformationFrameworkFacade);

        dataTypeFragmentMap = new HashMap<>();
        dataTypeFragmentMap.put(QUERY_TYPE, FRAGMENT_ID);
        assistedServiceCustomer360Controller.setDataTypeFragmentMap(dataTypeFragmentMap);

        dataTypeSectionMap = new HashMap<>();
        dataTypeSectionMap.put(QUERY_TYPE, SECTION_ID);
        assistedServiceCustomer360Controller.setDataTypeSectionMap(dataTypeSectionMap);
    }

    @Test
    public void testSearchCustomer360() {
        final Customer360QueryWsDTO customer360QueryWsDTO = new Customer360QueryWsDTO();
        customer360QueryWsDTO.setType(QUERY_TYPE);
        final Customer360QueryListWsDTO customer360QueryListWsDTO = new Customer360QueryListWsDTO();
        customer360QueryListWsDTO
                .setCustomer360Queries(Arrays.asList(customer360QueryWsDTO));

        final Fragment fragment = new Fragment();
        final StoreLocationData storeLocationData = new StoreLocationData();
        storeLocationData.setAddress(STORE_LOCATION_ADDRESS);
        fragment.setData(storeLocationData);

        final C360StoreLocation c360StoreLocation = new C360StoreLocation();
        c360StoreLocation.setAddress(STORE_LOCATION_ADDRESS);
        c360StoreLocation.setType(STORE_LOCATION_TYPE);

        when(additionalInformationFrameworkFacade.getFragment(eq(SECTION_ID), eq(FRAGMENT_ID), any()))
                .thenReturn(fragment);
        when(dataMapper.map(storeLocationData, Customer360DataWsDTO.class))
                .thenReturn(c360StoreLocation);

        final Customer360ListWsDTO response =  assistedServiceCustomer360Controller
                .searchCustomer360(BASE_SITE, USER_ID, customer360QueryListWsDTO);

        verify(additionalInformationFrameworkFacade).getFragment(eq(SECTION_ID), eq(FRAGMENT_ID), any());
        verify(dataMapper).map(storeLocationData, Customer360DataWsDTO.class);
        assertThat(response).isNotNull();
        assertThat(response.getValue()
                .get(0)).isSameAs(c360StoreLocation);
    }

    @Test
    public void testSearchCustomer360WhenValidationFailed() {
        final Customer360QueryWsDTO customer360QueryWsDTO = new Customer360QueryWsDTO();
        customer360QueryWsDTO.setType("invalidType");
        final Customer360QueryListWsDTO customer360QueryListWsDTO = new Customer360QueryListWsDTO();
        customer360QueryListWsDTO
                .setCustomer360Queries(Arrays.asList(customer360QueryWsDTO));

        doAnswer(invocationOnMock -> {
            final Errors errors = invocationOnMock.getArgument(1);
            errors.rejectValue("customer360Queries[0].type", "field.invalid");
            return null;
        }).when(customer360QueryListValidator)
                .validate(eq(customer360QueryListWsDTO), any());

        assertThatThrownBy(() -> assistedServiceCustomer360Controller
                .searchCustomer360(BASE_SITE, USER_ID, customer360QueryListWsDTO)
        ).isInstanceOf(WebserviceValidationException.class);
    }
}
