import { TypedMap } from '@smart/utils';
/**
 *  Used as support for legacy AngularJS templates in Angular components.
 *
 *  Compiles the template provided by the HTML Template string and scope.
 */
export declare class CompileHtmlDirective {
    /** HTML Template string to be compiled by directive e.g. `<div>some text</div>` */
    seCompileHtml: string;
    /** Data to be consumed by AngularJS template. */
    scope: TypedMap<any>;
}
