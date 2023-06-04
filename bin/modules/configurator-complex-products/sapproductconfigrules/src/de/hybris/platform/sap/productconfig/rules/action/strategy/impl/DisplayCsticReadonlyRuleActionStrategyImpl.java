/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.sap.productconfig.rules.action.strategy.impl;

import de.hybris.platform.ruleengineservices.rao.AbstractRuleActionRAO;
import de.hybris.platform.sap.productconfig.runtime.interf.model.ConfigModel;
import de.hybris.platform.sap.productconfig.runtime.interf.model.CsticModel;

import java.util.Map;



/**
 * flags the cstic identified by the action as read-only
 */
public class DisplayCsticReadonlyRuleActionStrategyImpl extends ProductConfigAbstractRuleActionStrategy
{


	private static final String ACTION_DESCRIPTION = "Setting characteristic read only";

	@Override
	protected boolean executeAction(final ConfigModel model, final AbstractRuleActionRAO action,
			final Map<String, CsticModel> csticMap)
	{
		getCstic(model, action, csticMap).setReadonly(true);
		return false;
	}

	@Override
	protected boolean isActionPossible(final ConfigModel model, final AbstractRuleActionRAO action,
			final Map<String, CsticModel> csticMap)
	{

		return getRuleActionChecker().checkCsticPartOfModel(model, action, ACTION_DESCRIPTION, csticMap);
	}

}
