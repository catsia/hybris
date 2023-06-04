/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 */
/* eslint-disable max-classes-per-file */
/*
 * Copyright (c) 2020 SAP SE or an SAP affiliate company. All rights reserved.
 */
/* tslint:disable:max-classes-per-file */

import { Injectable } from '@angular/core';
import { UpgradeModule } from '@angular/upgrade/static';
import * as lodash from 'lodash';
import {
    AbstractAngularJSBasedCustomElement,
    CustomElementConstructor,
    ELEMENT_UUID_ATTRIBUTE,
    ILegacyDecoratorToCustomElementConverter,
    NodeUtils,
    SeDowngradeService
} from 'smarteditcommons';

const CONTENT_PLACEHOLDER = 'CONTENT_PLACEHOLDER';

const scopes: string[] = [];

function angularJSDecoratorCustomElementClassFactory(
    upgrade: UpgradeModule,
    nodeUtils: NodeUtils
): CustomElementConstructor {
    return class extends AbstractAngularJSBasedCustomElement {
        private content: Node;

        /**
         * Avoid changing anything (no DOM changes) to the custom element in the constructor (Safari throw NotSupportedError).
         */
        constructor() {
            super(upgrade);
        }

        internalConnectedCallback(): void {
            this.markAsProcessed();

            try {
                nodeUtils.collectSmarteditAttributesByElementUuid(
                    this.getAttribute(ELEMENT_UUID_ATTRIBUTE)
                );
            } catch (e) {
                // the original component may have disappeared in the meantime
                return;
            }

            /* compile should only happen in one layer,
             * other layers will iteratively be compiled by their own custom element
             * these layers are therefore to be removed before compilation
             */
            this.content =
                this.content ||
                Array.from(this.childNodes).find(
                    (childNode: ChildNode) => childNode.nodeType === Node.ELEMENT_NODE
                );
            const placeholder = document.createElement(CONTENT_PLACEHOLDER);

            while (this.firstChild) {
                this.removeChild(this.firstChild);
            }

            this.appendChild(placeholder);
            this.setAttribute('active', 'active');
        }
    };
}

@SeDowngradeService(ILegacyDecoratorToCustomElementConverter)
@Injectable()
export class LegacyDecoratorToCustomElementConverter
    implements ILegacyDecoratorToCustomElementConverter {
    private convertedDecorators: string[] = [];

    constructor(private upgrade: UpgradeModule, private nodeUtils: NodeUtils) {}

    // for e2e purposes
    getScopes(): string[] {
        return scopes;
    }
    /*
     * Decorators are first class components:
     * even though they are built hierarchically, they are independant on one another and their scope should not be chained.
     * As a consequence, compiling them seperately is not an issue and thus enables converting them
     * to custom elements.
     */
    convert(_componentName: string): void {
        const componentName = _componentName.replace('se.', '');
        const originalName = lodash.kebabCase(componentName);
        if (!customElements.get(originalName)) {
            // may already have been defined through DI
            const CustomComponentClass = angularJSDecoratorCustomElementClassFactory(
                this.upgrade,
                this.nodeUtils
            ) as any;
            customElements.define(originalName, CustomComponentClass);
        }
    }

    convertIfNeeded(componentNames: string[]): void {
        componentNames.forEach((componentName) => {
            if (this.convertedDecorators.indexOf(componentName) === -1) {
                this.convertedDecorators.push(componentName);
                this.convert(componentName);
            }
        });
    }
}
