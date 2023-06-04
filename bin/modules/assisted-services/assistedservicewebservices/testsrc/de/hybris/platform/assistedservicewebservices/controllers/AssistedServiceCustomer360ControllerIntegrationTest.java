/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.assistedservicewebservices.controllers;

import static de.hybris.platform.webservicescommons.testsupport.client.WebservicesAssert.assertResponse;
import static org.fest.assertions.Assertions.assertThat;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.assistedservicewebservices.constants.AssistedservicewebservicesConstants;
import de.hybris.platform.assistedservicewebservices.dto.Customer360QueryListWsDTO;
import de.hybris.platform.assistedservicewebservices.dto.Customer360QueryWsDTO;
import de.hybris.platform.assistedservicewebservices.dto.C360ReviewList;
import de.hybris.platform.assistedservicewebservices.dto.C360StoreLocation;
import de.hybris.platform.assistedservicewebservices.dto.Customer360ListWsDTO;
import de.hybris.platform.oauth2.constants.OAuth2Constants;
import de.hybris.platform.webservicescommons.jaxb.Jaxb2HttpMessageConverter;
import de.hybris.platform.webservicescommons.testsupport.server.NeedsEmbeddedServer;
import org.junit.Test;

import javax.annotation.Resource;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.stream.StreamSource;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Arrays;


@NeedsEmbeddedServer(webExtensions =
        { AssistedservicewebservicesConstants.EXTENSIONNAME, OAuth2Constants.EXTENSIONNAME })
@IntegrationTest
public class AssistedServiceCustomer360ControllerIntegrationTest extends AbstractControllerIntegrationIntegrationTest{
    private static final String CUSTOMER_ID = "user1";
    private static final String CUSTOMER_PASSWORD = "123456";
    private static final String STORE_LOCATION_TYPE = "c360StoreLocation";
    private static final String REVIEW_LIST_TYPE = "c360ReviewList";

    @Resource(name = "jsonHttpMessageConverter")
    private Jaxb2HttpMessageConverter defaultJsonHttpMessageConverter;

    @Test
    public void shouldGetC360StoreLocation() throws JAXBException {
        final String path = "/"+ BASE_SITE_ID +"/users/" + CUSTOMER_ID + "/customer360";
        final Customer360QueryListWsDTO customer360QueryListWsDTO = new Customer360QueryListWsDTO();
        final Customer360QueryWsDTO customer360QueryWsDTO = new Customer360QueryWsDTO();

        customer360QueryWsDTO.setType(STORE_LOCATION_TYPE);
        customer360QueryListWsDTO.setCustomer360Queries(Arrays.asList(customer360QueryWsDTO));

        final Response response = getCustomer360WSCall(
                OAUTH_USERNAME,
                OAUTH_PASSWORD,
                path,
                customer360QueryListWsDTO
        );

        assertResponse(Response.Status.OK, response);

        final Customer360ListWsDTO entity = unmarshallResult(response, Customer360ListWsDTO.class);

        assertThat(entity.getValue()).hasSize(1);
        C360StoreLocation c360StoreLocation = (C360StoreLocation) entity.getValue().get(0);
        assertThat(c360StoreLocation.getAddress()).isEqualTo("New York United States of America 10019");
    }

    @Test
    public void shouldGetC360ReviewList() throws JAXBException
    {
        final String path = "/"+ BASE_SITE_ID +"/users/" + CUSTOMER_ID + "/customer360";
        final Customer360QueryListWsDTO customer360QueryListWsDTO = new Customer360QueryListWsDTO();
        final Customer360QueryWsDTO customer360QueryWsDTO = new Customer360QueryWsDTO();

        customer360QueryWsDTO.setType(REVIEW_LIST_TYPE);
        customer360QueryListWsDTO.setCustomer360Queries(Arrays.asList(customer360QueryWsDTO));

        final Response response = getCustomer360WSCall(
                OAUTH_USERNAME,
                OAUTH_PASSWORD,
                path,
                customer360QueryListWsDTO
        );

        assertResponse(Response.Status.OK, response);

        final Customer360ListWsDTO entity = unmarshallResult(response, Customer360ListWsDTO.class);

        assertThat(entity.getValue()).hasSize(1);
        C360ReviewList c360ReviewList = (C360ReviewList) entity.getValue().get(0);
        assertThat(c360ReviewList.getReviews()).hasSize(2);
    }

    @Test
    public void shouldGet403WhenMakingRequestAsCustomer() throws JAXBException
    {
        final String path = "/"+ BASE_SITE_ID +"/users/" + CUSTOMER_ID + "/customer360";
        final Customer360QueryListWsDTO customer360QueryListWsDTO = new Customer360QueryListWsDTO();
        final Customer360QueryWsDTO customer360QueryWsDTO = new Customer360QueryWsDTO();

        customer360QueryWsDTO.setType(STORE_LOCATION_TYPE);
        customer360QueryListWsDTO.setCustomer360Queries(Arrays.asList(customer360QueryWsDTO));

        final Response response = getCustomer360WSCall(
                CUSTOMER_ID,
                CUSTOMER_PASSWORD,
                path,
                customer360QueryListWsDTO
        );

        final Customer360ListWsDTO entity = unmarshallResult(response, Customer360ListWsDTO.class);
        assertThat(response.getStatus()).isEqualTo(403);
    }

    protected Response getCustomer360WSCall(final String username, final String password, final String path, final Customer360QueryListWsDTO customer360QueryListWsDTO) throws JAXBException
    {
        final Response result = getWsSecuredRequestBuilder(username, password)
                .path(path)
                .build()
                .accept(MediaType.APPLICATION_JSON)
                .post(Entity.entity(marshallDto(customer360QueryListWsDTO, Customer360QueryListWsDTO.class), MediaType.APPLICATION_JSON));
        result.bufferEntity();
        return result;
    }

    public String marshallDto(final Object input, final Class<?> c) throws JAXBException
    {
        final Marshaller marshaller = defaultJsonHttpMessageConverter.createMarshaller(c);
        final StringWriter writer = new StringWriter();
        marshaller.marshal(input, writer);
        return writer.toString();
    }

    protected <C> C unmarshallResult(final Response result, final Class<C> c) throws JAXBException
    {
        final Unmarshaller unmarshaller = defaultJsonHttpMessageConverter.createUnmarshaller(c);
        final StreamSource source = new StreamSource(result.readEntity(InputStream.class));
        final C entity = unmarshaller.unmarshal(source, c).getValue();
        return entity;
    }
}
