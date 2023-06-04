/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { functionsUtils } from '@smart/utils';
import { parseDirectiveName } from './SeDirective';
import { SeComponentConstructor, SeComponentDefinition } from './types';

/**
 * **Deprecated since 1905.**
 *
 * Class level typescript {@link http://www.typescriptlang.org/docs/handbook/decorators.html decorator factory}
 * used to declare a Smartedit web component from a dependency injection standpoint.
 * The controller alias will be $ctrl.
 * Inherits properties from {@link SeDirective}.
 *
 * @deprecated
 */
export const SeComponent = function (definition: SeComponentDefinition) {
    return function <T extends SeComponentConstructor>(componentConstructor: T): T {
        const nameSet = parseDirectiveName(definition.selector, componentConstructor);

        if (nameSet.restrict !== 'E') {
            const componentName = functionsUtils.getConstructorName(componentConstructor);
            throw new Error(
                `component ${componentName} declared a selector on class or attribute. version 1808 of Smartedit DI limits SeComponents to element selectors`
            );
        }

        componentConstructor.componentName = nameSet.name;

        // will be browsed by owning @SeModule
        componentConstructor.entryComponents = definition.entryComponents;
        componentConstructor.providers = definition.providers;

        return componentConstructor;
    };
};
