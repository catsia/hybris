/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.commerceservices.customer.impl;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.ServicelayerTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.commerceservices.customer.validator.CustomerSiteValidator;
import de.hybris.platform.commerceservices.customer.constraint.CustomerSite;

import javax.annotation.Resource;
import javax.validation.ConstraintValidatorContext;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


@IntegrationTest
public class CustomerSiteValidatorIntegrationTest extends ServicelayerTest
{
	private static final String testMulitiSiteEmployee = "testMulitiSiteEmployee";
	private static final String testEmployee = "testEmployee";
	private UserModel currentUser;
	private ConstraintValidatorContext context;
	private BaseSiteModel baseSiteModel;
	private CustomerSite customerSite;

	@Resource
	private ModelService modelService;

	@Resource
	private UserService userService;

	private CustomerSiteValidator customerSiteValidator = new CustomerSiteValidator();

	@Before
	public void setUp() throws Exception
	{
		createCoreData();
		importData("/impex/essentialdata_multisite.impex", "utf-8");
		importData("/commerceservices/test/multiSiteEmployeeTestData.impex", "utf-8");
		customerSiteValidator.initialize(customerSite);
	}

	@Test
	public void shouldGetValidateFalseWhenUseIsolatedSiteEmployeeWithSiteEmpty()
	{
		currentUser = userService.getUserForUID(testMulitiSiteEmployee);
		userService.setCurrentUser(currentUser);
		Assert.assertFalse(customerSiteValidator.isValid(baseSiteModel, context));
	}

	@Test
	public void shouldGetValidateTrueWhenUseIsolatedSiteEmployeeWithSiteNotEmpty()
	{
		currentUser = userService.getUserForUID(testMulitiSiteEmployee);
		userService.setCurrentUser(currentUser);
		baseSiteModel = modelService.create(BaseSiteModel.class);
		baseSiteModel.setUid("testSite");
		Assert.assertTrue(customerSiteValidator.isValid(baseSiteModel, context));
	}

	@Test
	public void shouldGetValidateTrueWhenUseNotIsolatedSiteEmployeeWithSiteEmpty()
	{
		currentUser = userService.getUserForUID(testEmployee);
		userService.setCurrentUser(currentUser);
		Assert.assertTrue(customerSiteValidator.isValid(baseSiteModel, context));
	}

	@Test
	public void shouldGetValidateTrueWhenUseNotIsolatedSiteEmployeeWithSiteNotEmpty()
	{
		currentUser = userService.getUserForUID(testEmployee);
		userService.setCurrentUser(currentUser);
		baseSiteModel = modelService.create(BaseSiteModel.class);
		baseSiteModel.setUid("testSite");
		Assert.assertTrue(customerSiteValidator.isValid(baseSiteModel, context));
	}
}
