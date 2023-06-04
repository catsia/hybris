/*
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.marketplaceaddon.renderer.impl;

import de.hybris.platform.addonsupport.renderer.impl.DefaultAddOnCMSComponentRenderer;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;
import de.hybris.platform.marketplaceaddon.constants.MarketplaceaddonConstants;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.jsp.PageContext;


public class MarketplaceAddonCMSComponentRenderer<C extends AbstractCMSComponentModel> extends DefaultAddOnCMSComponentRenderer<C>
{
	private static final String COMPONENT_ATTR = "component";

	@Override
	protected String getAddonUiExtensionName(final C component)
	{
		return MarketplaceaddonConstants.EXTENSIONNAME;
	}

	@Override
	public void renderComponent(final PageContext pageContext, final C component) throws ServletException, IOException
	{
		final Object existingComponentInRequest = pageContext.getAttribute(COMPONENT_ATTR, PageContext.REQUEST_SCOPE);

		pageContext.setAttribute(COMPONENT_ATTR, component, PageContext.REQUEST_SCOPE);

		try
		{
			super.renderComponent(pageContext, component);
		}
		finally
		{
			pageContext.removeAttribute(COMPONENT_ATTR, PageContext.REQUEST_SCOPE);
			if (existingComponentInRequest != null)
			{
				pageContext.setAttribute(COMPONENT_ATTR, existingComponentInRequest, PageContext.REQUEST_SCOPE);

			}
		}
	}
}
