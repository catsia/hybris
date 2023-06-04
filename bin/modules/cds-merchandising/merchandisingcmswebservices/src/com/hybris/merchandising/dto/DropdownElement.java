/**
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
package com.hybris.merchandising.dto;

import java.io.Serializable;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * An individual element of a SmartEdit drop down. SmartEdit requires it to have
 * both an ID (the value) and a label (the displayed text).
 */
@Schema(name = "strategy", description = "Strategy")
public class DropdownElement implements Serializable
{
	private static final long serialVersionUID = 1L;

	@Schema(name = "id", description = "Strategy identifier")
	String id;
	@Schema(name = "label", description = "Strategy label")
	String label;

	/**
	 * Default constructor.
	 */
	public DropdownElement()
	{
	}

	/**
	 * Constructor taking an ID and label.
	 *
	 * @param pId    The ID for the new element
	 * @param pLabel The label for the new element
	 */
	public DropdownElement(final String pId, final String pLabel)
	{
		id = pId;
		label = pLabel;
	}

	/**
	 * @return Id of the dropdown element
	 */
	public String getId()
	{
		return id;
	}

	/**
	 * @param id Accepts id as String and sets to the dropdown element
	 */
	public void setId(final String id)
	{
		this.id = id;
	}

	/**
	 * @return Label as String
	 */
	public String getLabel()
	{
		return label;
	}

	/**
	 * @param label Accepts Label as String and sets to the dropdown element
	 */
	public void setLabel(final String label)
	{
		this.label = label;
	}

}
