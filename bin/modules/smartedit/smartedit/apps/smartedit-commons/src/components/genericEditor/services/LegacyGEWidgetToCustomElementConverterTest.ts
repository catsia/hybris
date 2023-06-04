/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Injector } from '@angular/core';
import { UpgradeModule } from '@angular/upgrade/static';
import {
    stringUtils,
    GenericEditorField,
    IGenericEditor,
    LegacyGEWidgetToCustomElementConverter,
    TypedMap,
    WindowUtils
} from 'smarteditcommons';
import { GenericEditorFieldComponentScope } from '../components/GenericEditorFieldComponent';

/*
 * Semi integration test:
 * pure black box testing of real custom elements
 * but injected with mocks of AngularJS $compile and $rootscope
 */
describe('LegacyGEWidgetToCustomElementConverter test', () => {
    let $compile: jasmine.Spy;
    let upgrade: jasmine.SpyObj<UpgradeModule>;
    let injector: jasmine.SpyObj<Injector>;
    const windowUtils = jasmine.createSpyObj<WindowUtils>('windowUtils', ['isIframe']);

    let converter: LegacyGEWidgetToCustomElementConverter;

    let genericEditorField: HTMLElement & Partial<GenericEditorFieldComponentScope>;
    // parent of a se-template-ge-widget that has a generic-editor-field as ancestor
    let expectedParent: HTMLElement;
    // parent of a se-template-ge-widget that does not have a generic-editor-field as ancestor
    let unexpectedParent: HTMLElement;

    const templateGeWidget = document.createElement('se-template-ge-widget');

    const editor = jasmine.createSpyObj<IGenericEditor>('editor', ['submit']);
    const model: TypedMap<any> = {};
    const field = jasmine.createSpyObj<GenericEditorField>('field', ['options']);
    const qualifier = 'thequalifier';
    const id = 'theid';
    const editorStackId = 'theeditorStackId';
    const isFieldDisabled = () => false;


    beforeEach(() => {
        templateGeWidget.appendChild(document.createElement('noise'));

        upgrade = jasmine.createSpyObj<UpgradeModule>('UpgradeModule', ['bootstrap']);
        injector = jasmine.createSpyObj<Injector>('Injector', ['get']);
        (upgrade as any).injector = injector;


        $compile = jasmine.createSpy('$compile');

        const mocksMap = {
            $compile,
        } as TypedMap<any>;

        injector.get.and.callFake((name: any) => {
            return mocksMap[name];
        });

        windowUtils.isIframe.and.returnValue(false);

        converter = new LegacyGEWidgetToCustomElementConverter(upgrade, windowUtils);

        /*
         * custom element will only be declared once for all tests while the mock instances
         * are different each time, so test of scope and templateGeWidget in $compile must assess on deep equality
         * OR reuse same mock instances across all tests instead (and reset mocks)
         * OR use a beforeAll instead of beforeEach (and reset mocks)
         */
        converter.convert();

        genericEditorField = document.createElement('generic-editor-field');
        document.body.appendChild(genericEditorField);
        const child1 = document.createElement('child1');
        genericEditorField.appendChild(child1);
        expectedParent = document.createElement('expectedParent');
        genericEditorField.appendChild(expectedParent);

        unexpectedParent = document.createElement('unexpectedParent');
        document.body.appendChild(unexpectedParent);

        genericEditorField.editor = editor;
        genericEditorField.model = model;
        genericEditorField.field = field;
        genericEditorField.qualifier = qualifier;
        genericEditorField.id = id;
        genericEditorField.editorStackId = editorStackId;
        genericEditorField.isFieldDisabled = isFieldDisabled;
    });

    afterEach(() => {
        templateGeWidget.removeAttribute('processed');
        while (templateGeWidget.firstChild) {
            templateGeWidget.removeChild(templateGeWidget.firstChild);
        }
        while (document.body.firstChild) {
            document.body.removeChild(document.body.firstChild);
        }
    });

    it(`se-template-ge-widget child of an unexpected parent does not compile`, () => {
        unexpectedParent.appendChild(templateGeWidget);

        expect(stringUtils.formatHTML(templateGeWidget.outerHTML)).toEqual(
            stringUtils.formatHTML('<se-template-ge-widget><noise></noise></se-template-ge-widget>')
        );
    });
});
