/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Inject } from '@angular/core';
import {
    GatewayProxied,
    IComponentHandlerService,
    SeDowngradeService,
    YJQUERY_TOKEN
} from 'smarteditcommons';

@SeDowngradeService(IComponentHandlerService)
@GatewayProxied('isExternalComponent', 'reloadInner')
export class ComponentHandlerService extends IComponentHandlerService {
    constructor(@Inject(YJQUERY_TOKEN) yjQuery: JQueryStatic) {
        super(yjQuery);
    }
}
