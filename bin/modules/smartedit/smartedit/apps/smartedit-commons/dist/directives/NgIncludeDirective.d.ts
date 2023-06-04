import { TypedMap } from '@smart/utils';
/**
 *  Used as support for legacy AngularJS templates in Angular components.
 *
 *  Compiles the template provided by the templateUrl and scope.
 */
export declare class NgIncludeDirective {
    /** Template URL to be compiled by directive e.g. `MyComponentTemplate.html` */
    ngInclude: string;
    /** Data to be consumed by AngularJS template */
    scope: TypedMap<any>;
}
