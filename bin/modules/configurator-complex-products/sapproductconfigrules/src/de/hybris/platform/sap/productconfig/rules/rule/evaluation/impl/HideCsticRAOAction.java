/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.productconfig.rules.rule.evaluation.impl;

import de.hybris.platform.ruleengineservices.rao.ProcessStep;
import de.hybris.platform.ruleengineservices.rule.evaluation.RuleActionContext;
import de.hybris.platform.sap.productconfig.rules.rao.CsticRAO;
import de.hybris.platform.sap.productconfig.rules.rao.action.HideCsticRAO;

import java.util.Map;


/**
 * Encapsulates logic of hiding characteristic as rule action.
 */
public class HideCsticRAOAction extends ProductConfigAbstractRAOAction
{

	@Override
	public boolean performActionInternal(final RuleActionContext context)
	{
		final Map<String, Object> parameters = context.getParameters();
		logRuleData(context, parameters, CSTIC_NAME);
		boolean processed = false;

		if (validateProcessStep(context, parameters, ProcessStep.RETRIEVE_CONFIGURATION))
		{
			final CsticRAO csticRao = createCsticRAO(parameters);
			final HideCsticRAO hideCsticRAO = new HideCsticRAO();
			hideCsticRAO.setAppliedToObject(csticRao);

			updateContext(context, hideCsticRAO);
			processed = true;
		}
		return processed;
	}

	@Override
	protected String prepareActionLogText(final RuleActionContext context, final Map<String, Object> parameters)
	{
		final String csticName = (String) parameters.get(CSTIC_NAME);
		return "Hence skipping hide characteristic for cstic  " + csticName + ".";
	}
}
