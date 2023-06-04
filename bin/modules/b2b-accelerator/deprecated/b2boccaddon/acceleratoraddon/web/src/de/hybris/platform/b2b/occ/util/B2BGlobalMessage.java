/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.b2b.occ.util;

import java.io.Serializable;
import java.util.Collection;

public class B2BGlobalMessage implements Serializable
{
    private String code;
    private transient Collection<Object> attributes;

    public String getCode()
    {
        return code;
    }

    public void setCode(final String code)
    {
        this.code = code;
    }

    public Collection<Object> getAttributes()
    {
        return attributes;
    }

    public void setAttributes(final Collection<Object> attributes)
    {
        this.attributes = attributes;
    }
}
