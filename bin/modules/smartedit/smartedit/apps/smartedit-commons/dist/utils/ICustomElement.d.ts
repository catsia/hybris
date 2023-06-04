import { UpgradeModule } from '@angular/upgrade/static';
export interface ICustomElement {
    connectedCallback?(): void;
    disconnectedCallback?(): void;
    attributeChangedCallback?(name: string, oldValue: any, newValue: any): void;
}
export declare type CustomElementConstructor = new (...arg: any[]) => ICustomElement;
export declare abstract class AbstractAngularJSBasedCustomElement extends HTMLElement implements ICustomElement {
    private upgrade;
    private PROCESSED_ATTRIBUTE_NAME;
    abstract internalConnectedCallback(): void;
    constructor(upgrade: UpgradeModule);
    internalAttributeChangedCallback?(name: string, oldValue: any, newValue: any): void;
    internalDisconnectedCallback?(): void;
    markAsProcessed(): void;
    connectedCallback(): void;
    disconnectedCallback(): void;
    attributeChangedCallback(name: string, oldValue: any, newValue: any): void;
    private shouldReactOnAttributeChange;
}
