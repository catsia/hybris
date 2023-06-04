/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Dictionary } from 'lodash';
import { PersonalizationsmarteditCustomizeViewHelper } from 'personalizationsmartedit/service/PersonalizationsmarteditCustomizeViewHelper';
import { GatewayProxied, SeDowngradeService } from 'smarteditcommons';
@SeDowngradeService()
@GatewayProxied('getSourceContainersInfo')
export class CustomizeViewServiceProxy {
    constructor(
        protected personalizationsmarteditCustomizeViewHelper: PersonalizationsmarteditCustomizeViewHelper
    ) {}

    public getSourceContainersInfo(): Dictionary<number> {
        return this.personalizationsmarteditCustomizeViewHelper.getSourceContainersInfo();
    }
}
