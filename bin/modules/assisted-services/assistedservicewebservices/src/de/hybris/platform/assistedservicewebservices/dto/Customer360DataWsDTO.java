/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.assistedservicewebservices.dto;

import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Representation of the Customer 360 data.
 */
@Schema(
        name="Customer360Data",
        description="Representation of the Customer 360 data.",
        oneOf = {
                C360ReviewList.class,
                C360StoreLocation.class,
        }
)
public  class Customer360DataWsDTO implements Serializable

{

    /** Default serialVersionUID value. */

    private static final long serialVersionUID = 1L;

    /** Type of the customer 360 data returned<br/><br/><i>Generated property</i> for <code>Customer360DataWsDTO.type</code> property defined at extension <code>assistedservicewebservices</code>. */
    @Schema(name="type", description="Type of the returned Customer 360 data e.g. 'c360StoreLocation', 'c360ReviewList'.", example = "c360StoreLocation")
    private String type;

    public Customer360DataWsDTO()
    {
        // default constructor
    }

    public void setType(final String type)
    {
        this.type = type;
    }

    public String getType()
    {
        return type;
    }
}
