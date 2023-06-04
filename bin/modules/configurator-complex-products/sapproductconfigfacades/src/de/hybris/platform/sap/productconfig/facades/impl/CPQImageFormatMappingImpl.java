/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.productconfig.facades.impl;

import de.hybris.platform.sap.productconfig.facades.CPQImageFormatMapping;
import de.hybris.platform.sap.productconfig.facades.CPQImageType;

import java.util.Map;


/**
 * Default implementation of the {@link CPQImageFormatMapping}.<br>
 */
public class CPQImageFormatMappingImpl implements CPQImageFormatMapping
{
	private Map<String, CPQImageType> mapping;

	/**
	 * @param mapping
	 *           image format mapping for CPQ
	 */
	public void setMapping(final Map<String, CPQImageType> mapping)
	{
		this.mapping = mapping;
	}

	@Override
	public Map<String, CPQImageType> getCPQMediaFormatQualifiers()
	{
		return mapping;
	}

}
