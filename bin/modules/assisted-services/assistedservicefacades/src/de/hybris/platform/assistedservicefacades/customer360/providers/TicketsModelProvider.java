/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.assistedservicefacades.customer360.providers;

import de.hybris.platform.assistedservicefacades.customer360.FragmentModelProvider;
import de.hybris.platform.assistedservicefacades.customer360.GeneralActivityData;
import de.hybris.platform.assistedservicefacades.customer360.GeneralActivityDataList;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.servicelayer.dto.converter.Converter;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.ticket.model.CsTicketModel;
import de.hybris.platform.ticket.service.TicketService;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Model provider implementation for Support Tickets fragment.
 */
public class TicketsModelProvider implements FragmentModelProvider<GeneralActivityDataList>
{
	protected static final int MAX_AMOUNT = 10;
	private UserService userService;
	private TicketService ticketService;
	private Converter<CsTicketModel, GeneralActivityData> ticketConverter;

	@Override
	public GeneralActivityDataList getModel(final Map<String, String> parameters)
	{
		final CustomerModel user = (CustomerModel) getUserService().getCurrentUser();
		final List<CsTicketModel> ticketModel = getTicketService().getTicketsForCustomer(user);

		final GeneralActivityDataList generalActivityDataList = new GeneralActivityDataList();
		generalActivityDataList.setGeneralActivities(
				ticketModel.stream().sorted(Comparator.comparing(CsTicketModel::getModifiedtime).reversed())
						.limit(getEventNumberLimit()).map(getTicketConverter()::convert).collect(Collectors.toList())
		);
		return generalActivityDataList;
	}

	protected UserService getUserService()
	{
		return userService;
	}

	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	protected TicketService getTicketService()
	{
		return ticketService;
	}

	public void setTicketService(final TicketService ticketService)
	{
		this.ticketService = ticketService;
	}

	/**
	 * @return the ticketConverter
	 */
	public Converter<CsTicketModel, GeneralActivityData> getTicketConverter()
	{
		return ticketConverter;
	}

	/**
	 * @param ticketConverter
	 *           the ticketConverter to set
	 */
	public void setTicketConverter(final Converter<CsTicketModel, GeneralActivityData> ticketConverter)
	{
		this.ticketConverter = ticketConverter;
	}

	protected int getEventNumberLimit()
	{
		return MAX_AMOUNT;
	}
}
