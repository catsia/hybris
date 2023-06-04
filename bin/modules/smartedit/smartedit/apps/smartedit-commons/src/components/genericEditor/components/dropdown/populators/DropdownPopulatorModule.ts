/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 */
/* eslint-disable max-classes-per-file */
/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { NgModule } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import * as lodash from 'lodash';

import { LanguageService } from '../../../../../services';
import { DropdownPopulatorInterface } from './DropdownPopulatorInterface';
import { OptionsDropdownPopulator } from './options';
import { IDropdownPopulatorInterface } from './types';
import { UriDropdownPopulator } from './uri';

/**
 * For AngularJS, Custom Dropdown Populator classes extend the DropdownPopulatorInterface,
 * so here we return constructor function with pre-set dependencies.
 */
const dropdownPopulatorInterfaceConstructorFactory = (
    languageService: LanguageService,
    translateService: TranslateService
): new () => any =>
    class extends DropdownPopulatorInterface {
        constructor() {
            super(lodash, languageService, translateService);
        }
    };

@NgModule({
    providers: [
        OptionsDropdownPopulator,
        UriDropdownPopulator,
        {
            // required for AngularJS
            provide: IDropdownPopulatorInterface,
            useFactory: dropdownPopulatorInterfaceConstructorFactory,
            deps: [LanguageService, TranslateService]
        }
    ]
})
export class DropdownPopulatorModule {}
