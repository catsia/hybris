/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.assistedserviceservices.impl;

import static de.hybris.platform.assistedserviceservices.impl.DefaultAssistedServiceService.NOT_ANONYMOUS_CART_ERROR;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import de.hybris.bootstrap.annotations.IntegrationTest;
import de.hybris.platform.assistedserviceservices.exception.AssistedServiceException;
import de.hybris.platform.assistedserviceservices.utils.AssistedServiceSession;
import de.hybris.platform.basecommerce.model.site.BaseSiteModel;
import de.hybris.platform.core.model.order.AbstractOrderEntryModel;
import de.hybris.platform.core.model.order.CartModel;
import de.hybris.platform.core.model.user.CustomerModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.ServicelayerTransactionalTest;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.session.SessionService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.site.BaseSiteService;
import de.hybris.platform.util.localization.Localization;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;


/**
 * Test class for {@link DefaultAssistedServiceService}
 */
@IntegrationTest
public class AssistedServiceServiceTest extends ServicelayerTransactionalTest
{
	private final String customerUID = "ascustomer";
	private final String firstCart = "as00000001";
	private final String secondCart = "as00000002";
	private final String anonymousCart = "an00000010";

	@Resource
	private DefaultAssistedServiceService assistedServiceService;
	@Resource
	private BaseSiteService baseSiteService;
	@Resource
	private UserService userService;
	@Resource
	private ModelService modelService;
	@Resource
	private CartService cartService;

	@Resource
	private SessionService sessionService;

	@Before
	public void setup() throws Exception
	{
		importCsv("/assistedserviceservices/test/cart_data.impex", "UTF-8");
		baseSiteService.setCurrentBaseSite("testSite", true);
		baseSiteService.getCurrentBaseSite().setDataIsolationEnabled(Boolean.valueOf(false));
		userService.setCurrentUser(userService.getUserForUID(customerUID));
		((CustomerModel) userService.getCurrentUser()).setSite(null);
		sessionService.setAttribute("ASM", new AssistedServiceSession());
		importCsv("/assistedserviceservices/test/multisite_data.impex", "UTF-8");
	}

	@Test
	public void latestModifiedCartTest()
	{
		assertEquals(secondCart, assistedServiceService.getLatestModifiedCart(userService.getUserForUID("ascustomer")).getCode());
		assertEquals("as00000003",
				assistedServiceService.getLatestModifiedCart(userService.getUserForUID("ascustomer2")).getCode());
		assertNull(assistedServiceService.getLatestModifiedCart(userService.getUserForUID("ascustomer3")));
	}

	@Test
	public void testGetCartByCode()
	{
		final CartModel firstCartModel = assistedServiceService.getCartByCode(firstCart, userService.getUserForUID(customerUID));
		assertEquals(firstCart, firstCartModel.getCode());

		final CartModel secondCartModel = assistedServiceService.getCartByCode(secondCart, userService.getUserForUID(customerUID));
		assertEquals(secondCart, secondCartModel.getCode());
	}

	@Test
	public void testGetCarts()
	{
		final Collection<CartModel> cartsForCustomer = assistedServiceService.getCartsForCustomer(
				(CustomerModel) userService.getUserForUID(customerUID));
		assertTrue(cartsForCustomer.stream().anyMatch(cartModel -> cartModel.getCode().equals(firstCart)));
		assertTrue(cartsForCustomer.stream().anyMatch(cartModel -> cartModel.getCode().equals(secondCart)));
	}

	@Test
	public void restoreCartToUserTest()
	{
		final CartService spyService = spy(cartService);
		final ModelService modelServiceSpy = spy(modelService);
		final UserModel user = userService.getUserForUID(customerUID);
		final CartModel cart = cartService.getSessionCart();
		cart.setEntries(new ArrayList<>());
		assistedServiceService.setCartService(spyService);
		assistedServiceService.setModelService(modelServiceSpy);
		assistedServiceService.restoreCartToUser(null, null);
		assistedServiceService.restoreCartToUser(cart, null);
		assistedServiceService.restoreCartToUser(cart, user);
		verify(spyService, never()).changeCurrentCartUser(nullable(UserModel.class));
		final List<AbstractOrderEntryModel> entries = new ArrayList<>();
		entries.add(new AbstractOrderEntryModel());
		cart.setEntries(entries);
		doNothing().when(spyService).changeCurrentCartUser(nullable(UserModel.class));
		doNothing().when(modelServiceSpy).refresh(nullable(Object.class));
		assistedServiceService.restoreCartToUser(cart, user);
		verify(spyService).changeCurrentCartUser(nullable(UserModel.class));
		verify(modelServiceSpy).refresh(nullable(Object.class));
	}

	@Test
	public void bindCustomerToCartTest()
	{
		final UserModel user = userService.getUserForUID(customerUID);
		final CartModel cart = assistedServiceService.getCartByCode(firstCart, user);
		cartService.setSessionCart(cart);

		assertThrows(Localization.getLocalizedString(NOT_ANONYMOUS_CART_ERROR), AssistedServiceException.class,
				() -> assistedServiceService.bindCustomerToCart(null, firstCart));
	}

	@Test
	public void bindCustomerToCartTestWithAnonymousCart() throws AssistedServiceException
	{
		final UserModel user = userService.getUserForUID(customerUID);
		final CartModel cart = assistedServiceService.getCartByCode(anonymousCart, user);
		cartService.setSessionCart(cart);

		assistedServiceService.bindCustomerToCart(null, anonymousCart);
	}

	@Test
	public void bindCustomerToCartWithIsolatedSiteAndCommonCustomerTest()
	{
		final UserModel user = userService.getUserForUID(customerUID);
		final CartModel cart = assistedServiceService.getCartByCode(firstCart, user);
		cartService.setSessionCart(cart);
		baseSiteService.getCurrentBaseSite().setDataIsolationEnabled(Boolean.valueOf(true));

		assertThrows(Localization.getLocalizedString(DefaultAssistedServiceService.CUSTOMER_NOT_FOUND),
				AssistedServiceException.class, () -> assistedServiceService.bindCustomerToCart(null, firstCart));
	}

	@Test
	public void bindCustomerToCartWithCommonSiteAndIsolatedCustomerTest() throws AssistedServiceException
	{
		final UserModel user = userService.getUserForUID(customerUID);
		final CartModel cart = assistedServiceService.getCartByCode(firstCart, user);
		cartService.setSessionCart(cart);
		((CustomerModel) user).setSite(baseSiteService.getCurrentBaseSite());

		assertThrows(Localization.getLocalizedString(DefaultAssistedServiceService.CUSTOMER_NOT_FOUND),
				AssistedServiceException.class, () -> assistedServiceService.bindCustomerToCart(null, firstCart));
	}

	@Test
	public void bindCustomerToCartWithIsolatedSiteAndIsolatedCustomerTestInCorrectSite()
	{
		final UserModel user = userService.getUserForUID(customerUID);
		final CartModel cart = assistedServiceService.getCartByCode(firstCart, user);
		cartService.setSessionCart(cart);
		((CustomerModel) user).setSite(new BaseSiteModel("differentSite"));
		baseSiteService.getCurrentBaseSite().setDataIsolationEnabled(Boolean.valueOf(true));

		assertThrows(Localization.getLocalizedString(DefaultAssistedServiceService.CUSTOMER_NOT_FOUND),
				AssistedServiceException.class, () -> assistedServiceService.bindCustomerToCart(null, firstCart));

	}

	@Test
	public void bindCustomerToCartWithIsolatedSiteAndIsolatedCustomerWithCommonCartTest() throws AssistedServiceException
	{
		final UserModel user = userService.getUserForUID(customerUID);
		((CustomerModel) user).setSite(baseSiteService.getCurrentBaseSite());
		baseSiteService.getCurrentBaseSite().setDataIsolationEnabled(Boolean.valueOf(true));

		assertThrows(Localization.getLocalizedString(NOT_ANONYMOUS_CART_ERROR), AssistedServiceException.class,
				() -> assistedServiceService.bindCustomerToCart(null, firstCart));
	}

	@Test
	public void bindCustomerToCartWithIsolatedSiteAndIsolatedCustomerWithAnonymousCartTest() throws AssistedServiceException
	{
		final UserModel user = userService.getUserForUID(customerUID);
		((CustomerModel) user).setSite(baseSiteService.getCurrentBaseSite());
		baseSiteService.getCurrentBaseSite().setDataIsolationEnabled(Boolean.valueOf(true));

		assistedServiceService.bindCustomerToCart(null, anonymousCart);

		final CartModel cart = cartService.getSessionCart();
		assertEquals(customerUID, cart.getUser().getUid());
	}

	@Test
	public void noneIsolatedAgentLoginIsolatedSiteTest()
	{
		final UserModel user = userService.getUserForUID("customer.support.none_isolated@nakano.com");
		baseSiteService.setCurrentBaseSite(baseSiteService.getBaseSiteForUID("test-site-a"), false);

		final boolean isLogin = assistedServiceService.isAgentCanLogin(user);
		assertTrue(isLogin);
	}

	@Test
	public void noneIsolatedAgentLoginNoneIsolatedSiteTest()
	{
		final UserModel user = userService.getUserForUID("customer.support.none_isolated@nakano.com");

		final boolean isLogin = assistedServiceService.isAgentCanLogin(user);
		assertTrue(isLogin);
	}

	@Test
	public void isolatedAgentLoginNoneIsolatedSiteTest()
	{
		final UserModel user = userService.getUserForUID("customer.support.standalone_site_a@nakano.com");

		final boolean isLogin = assistedServiceService.isAgentCanLogin(user);
		assertFalse(isLogin);
	}

	@Test
	public void isolatedAgentLoginAssignedIsolatedSiteTest()
	{
		final UserModel user = userService.getUserForUID("customer.support.standalone_site_a@nakano.com");
		baseSiteService.setCurrentBaseSite(baseSiteService.getBaseSiteForUID("test-site-a"), false);

		final boolean isLogin = assistedServiceService.isAgentCanLogin(user);
		assertTrue(isLogin);
	}

	@Test
	public void isolatedAgentLoginNotAssignedIsolatedSiteTest()
	{
		final UserModel user = userService.getUserForUID("customer.support.standalone_site_a@nakano.com");
		baseSiteService.setCurrentBaseSite(baseSiteService.getBaseSiteForUID("test-site-b"), false);

		final boolean isLogin = assistedServiceService.isAgentCanLogin(user);
		assertFalse(isLogin);
	}

	@Test
	public void isolatedAgentWithMultiGroupLoginAssignedIsolatedSiteTest()
	{
		final UserModel user = userService.getUserForUID("customer.support.standalone_site_all@nakano.com");
		baseSiteService.setCurrentBaseSite(baseSiteService.getBaseSiteForUID("test-site-a"), false);

		boolean isLogin = assistedServiceService.isAgentCanLogin(user);
		assertTrue(isLogin);

		baseSiteService.setCurrentBaseSite(baseSiteService.getBaseSiteForUID("test-site-b"), false);
		isLogin = assistedServiceService.isAgentCanLogin(user);
		assertTrue(isLogin);
	}
}
