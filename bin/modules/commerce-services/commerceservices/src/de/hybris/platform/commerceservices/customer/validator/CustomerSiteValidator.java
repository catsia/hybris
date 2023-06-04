/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.customer.validator;

import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.commerceservices.customer.constraint.CustomerSite;
import de.hybris.platform.core.Registry;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import de.hybris.platform.core.model.user.UserGroupModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.config.ConfigurationService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.commerceservices.constants.CommerceServicesConstants;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;


public class CustomerSiteValidator implements ConstraintValidator<CustomerSite, BaseSiteModel>
{
	private UserService userService;

	private ConfigurationService configurationService;

	@Override
	public void initialize(final CustomerSite constraintAnnotation)
	{
		userService = Registry.getApplicationContext().getBean("userService", UserService.class);
		configurationService = Registry.getApplicationContext().getBean("configurationService", ConfigurationService.class);
	}

	@Override
	public boolean isValid(final BaseSiteModel baseSiteModel, final ConstraintValidatorContext context)
	{
		return notBelongToMultiSiteGroup() || baseSiteModel != null;
	}

	private boolean notBelongToMultiSiteGroup()
	{
		final String multiSiteGroupName = getConfigurationService().getConfiguration()
				.getString(CommerceServicesConstants.MULTI_SITE_GROUP_GROUP_DEFAULT,
						CommerceServicesConstants.MULTI_SITE_GROUP_DEFAULT_NAME);

		final UserModel currentUser = getUserService().getCurrentUser();
		final Set<UserGroupModel> allUserGroups = getUserService().getAllUserGroupsForUser(currentUser);

		final List<UserGroupModel> multiSiteGroup = allUserGroups.stream()
				.filter(userGroupModel -> StringUtils.equals(userGroupModel.getUid(), multiSiteGroupName))
				.collect(Collectors.toList());

		return multiSiteGroup.isEmpty();
	}

	protected UserService getUserService()
	{
		return userService;
	}

	protected ConfigurationService getConfigurationService()
	{
		return configurationService;
	}
}
