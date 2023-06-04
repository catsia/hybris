/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Directive, Input } from '@angular/core';
import { TypedMap } from '@smart/utils';

/**
 *  Used as support for legacy AngularJS templates in Angular components.
 *
 *  Compiles the template provided by the templateUrl and scope.
 */
@Directive({ selector: '[ngInclude]' })
export class NgIncludeDirective {
    /** Template URL to be compiled by directive e.g. `MyComponentTemplate.html` */
    @Input() ngInclude: string;

    /** Data to be consumed by AngularJS template */
    @Input() scope: TypedMap<any>;
}
