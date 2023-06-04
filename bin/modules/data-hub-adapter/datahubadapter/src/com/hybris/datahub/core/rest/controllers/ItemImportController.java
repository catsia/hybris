/*
 * [y] hybris Platform
 *
 * Copyright (c) 2018 SAP SE or an SAP affiliate company.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of SAP
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with SAP.
 */

package com.hybris.datahub.core.rest.controllers;

import de.hybris.platform.core.model.c2l.LanguageModel;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.servicelayer.session.SessionService;

import com.hybris.datahub.core.dto.ItemImportTaskData;
import com.hybris.datahub.core.facades.ItemImportTaskRunningFacade;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.google.common.base.Preconditions;


/**
 * A REST resource for uploading and executing IMPEX files.
 */
@Controller
@RequestMapping({"/import/"})
public class ItemImportController
{
	private static final Logger LOGGER = LoggerFactory.getLogger(ItemImportController.class);
	private static final int BUFFER_SIZE = 1024;

	@Autowired
	private ItemImportTaskRunningFacade importTaskRunningFacade;

	@Autowired
	private SessionService sessionService;

	/**
	 * Performs upload of an ImpEx file and import of the items containing in the file.
	 *
	 * @param poolName pool whose items are being imported
	 * @param publicationId the publicationId
	 * @param resultCallbackUrl the callback URL to use when returning the <code>ItemImportResult</code>
	 * @param inputStream input stream to the impex script
	 * @throws IOException
	 */
	@RequestMapping(value = "/pools/{poolName}/publications/{publicationId}", method = RequestMethod.PUT, consumes = "application/octet-stream", produces = {"application/json", "application/xml"})
	@ResponseStatus(value = HttpStatus.OK)
	public void importFromStream(@PathVariable("poolName") final String poolName, @PathVariable("publicationId") final Long publicationId, @RequestHeader(value = "Result-Callback-URL") final String resultCallbackUrl, final InputStream inputStream)
			throws IOException
	{
		Preconditions.checkArgument(StringUtils.isNotEmpty(poolName), "poolName cannot be null");
		Preconditions.checkArgument(publicationId != null, "publicationId cannot be null");
		Preconditions.checkArgument(StringUtils.isNotEmpty(resultCallbackUrl), "resultCallbackUrl header cannot be null");
		Preconditions.checkArgument(inputStream != null, "inputStream cannot be null");

		LOGGER.info("Received request PUT: /import/pools/{}/publications/{}", poolName, publicationId);
		LOGGER.info("resultCallbackUrl: {}", resultCallbackUrl);

		try
		{
			final ItemImportTaskData taskData = new ItemImportTaskData();
			taskData.setPoolName(poolName);
			taskData.setPublicationId(publicationId);
			taskData.setResultCallbackUrl(resultCallbackUrl);
			taskData.setImpexMetaData(inputStreamToByteArray(inputStream));
			// we are purposely only getting 'user' and 'language' attributes because that's all we need
			final UserModel user = sessionService.getCurrentSession().getAttribute("user");
			final LanguageModel language = sessionService.getCurrentSession().getAttribute("language");
			taskData.addSessionAttribute("user", user);
			taskData.addSessionAttribute("language", language);

			importTaskRunningFacade.scheduleImportTask(taskData);
		}
		finally
		{
			inputStream.close();
		}
	}

	protected byte[] inputStreamToByteArray(final InputStream inputStream) throws IOException
	{
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();

		final byte[] buffer = new byte[BUFFER_SIZE];
		int len;
		while ((len = inputStream.read(buffer)) > -1)
		{
			baos.write(buffer, 0, len);
		}
		baos.flush();

		return baos.toByteArray();
	}

	@ExceptionHandler(IllegalArgumentException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	private static void handleIllegalArgumentException(final Exception e, final HttpServletRequest request, final Writer writer)
	{
		/*do nothing*/
	}
}
