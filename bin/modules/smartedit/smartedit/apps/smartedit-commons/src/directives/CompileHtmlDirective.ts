/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Directive, Input } from '@angular/core';
import { TypedMap } from '@smart/utils';

/**
 *  Used as support for legacy AngularJS templates in Angular components.
 *
 *  Compiles the template provided by the HTML Template string and scope.
 */
@Directive({ selector: '[seCompileHtml]' })
export class CompileHtmlDirective {
    /** HTML Template string to be compiled by directive e.g. `<div>some text</div>` */
    @Input() seCompileHtml: string;
    /** Data to be consumed by AngularJS template. */
    @Input() scope: TypedMap<any>;
}
