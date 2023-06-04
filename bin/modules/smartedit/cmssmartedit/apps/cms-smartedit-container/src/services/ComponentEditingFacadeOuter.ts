/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { IComponentEditingFacade } from 'cmscommons';
import { GatewayProxied, SeDowngradeService } from 'smarteditcommons';

@SeDowngradeService(IComponentEditingFacade)
@GatewayProxied(
    'addNewComponentToSlot',
    'addExistingComponentToSlot',
    'cloneExistingComponentToSlot',
    'moveComponent'
)
export class ComponentEditingFacade extends IComponentEditingFacade {}
