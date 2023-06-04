/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.inboundservices.persistence.populator;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.inboundservices.persistence.PersistenceContext;
import de.hybris.platform.integrationservices.model.TypeAttributeDescriptor;

import java.util.Map;

import com.google.common.collect.Maps;

/**
 * This populator appends to the content of the map attribute
 */
public final class MapAttributePopulator extends AbstractMapAttributePopulator
{
	@Override
	protected boolean isApplicable(final TypeAttributeDescriptor attribute, final PersistenceContext context)
	{
		return attribute.isMap() && !attribute.isLocalized() && !context.isReplaceAttributes();
	}

	protected void setNewMapValues(final ItemModel item, final TypeAttributeDescriptor attribute, final Map<?, ?> newValue)
	{
		final var value = attribute.accessor().getValue(item);
		final var existingValues = value != null ? Maps.newHashMap((Map<Object, Object>) value) : Maps.newHashMap();
		final Map<?, ?> newValues = newValue;
		existingValues.putAll(newValues);
		attribute.accessor().setValue(item, existingValues);
	}
}
