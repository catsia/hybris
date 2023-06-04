/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.cms2lib.jalo;

import static org.junit.Assert.assertTrue;

import de.hybris.platform.testframework.HybrisJUnit4TransactionalTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * JUnit Tests for the Cms2lib extension
 */
public class Cms2libTest extends HybrisJUnit4TransactionalTest
{
	@Before
	public void setUp()
	{
		// implement here code executed before each test
	}

	@After
	public void tearDown()
	{
		// implement here code executed after each test
	}

	/**
	 * This is a sample test method.
	 */
	@Test
	public void testCms2lib()
	{
		final boolean testTrue = true;
		assertTrue("true is not true", testTrue);
	}
}
