/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 */
/**
 * Copyright (c) 2019 SAP SE or an SAP affiliate company. All rights reserved.
 * @module smartutils
 */
import 'jasmine';
import { Type } from '@angular/core';
import { DialogRef } from '@fundamental-ngx/core';
import { combineLatest } from 'rxjs';
import { TypedMap } from '../../dtos';
import { ModalButtonAction, ModalButtonOptions } from '../../interfaces';
import { ModalManagerService } from './modal-manager.service';

const buttons: ModalButtonOptions[] = [{ id: 'id', label: 'label' }];
const title = 'My Title';
const isDismissButtonVisible = true;
const modalData: TypedMap<string> = { myData: 'myData' };
const component: Type<any> = {} as Type<any>;

function createModalRefMock(): jasmine.SpyObj<DialogRef> {
    return {
        ...jasmine.createSpyObj('modalRef', ['close', 'dismiss']),
        data: {
            component,
            modalData,
            templateConfig: {
                isDismissButtonVisible,
                title,
                buttons
            }
        }
    };
}

describe('Modal Manager', () => {
    let modalManager: ModalManagerService;
    let modalRef: jasmine.SpyObj<DialogRef>;

    beforeEach(() => {
        modalRef = createModalRefMock();
        modalManager = new ModalManagerService(modalRef);
    });

    it('initializes with correct data', () => {
        modalManager.init();

        combineLatest([
            modalManager.getButtons(),
            modalManager.getTitle(),
            modalManager.getComponent(),
            modalManager.getModalData(),
            modalManager.getIsDismissButtonVisible()
        ]).subscribe(([_buttons, _title, _component, _modalData, _isDismissButtonVisible]) => {
            expect(_buttons).toEqual(buttons);
            expect(_title).toEqual(title);
            expect(_component).toEqual(component);
            expect(_modalData).toEqual(modalData);
            expect(_isDismissButtonVisible).toEqual(isDismissButtonVisible);
        });
    });

    describe('adds button/s correctly', () => {
        it('addButton', () => {
            const newButton: ModalButtonOptions = { id: 'newId', label: 'newLabel' };

            modalManager.init();

            modalManager.addButton(newButton);
            modalManager.getButtons().subscribe((_buttons: ModalButtonOptions[]) => {
                expect(_buttons).toEqual([...buttons, newButton]);
            });
        });

        it('addButtons', () => {
            const newButtons: ModalButtonOptions[] = [
                { id: 'id1', label: 'label1' },
                { id: 'id2', label: 'label2' }
            ];

            modalManager.init();

            modalManager.addButtons(newButtons);
            modalManager.getButtons().subscribe((_buttons: ModalButtonOptions[]) => {
                expect(_buttons).toEqual([...buttons, ...newButtons]);
            });
        });
    });

    it('removes button correctly', () => {
        modalManager.init();

        modalManager.removeButton('id');
        modalManager.getButtons().subscribe((_buttons: ModalButtonOptions[]) => {
            expect(_buttons).toEqual([]);
        });
    });

    it('removes all buttons correctly', () => {
        modalManager.init();

        modalManager.removeAllButtons();
        modalManager.getButtons().subscribe((_buttons: ModalButtonOptions[]) => {
            expect(_buttons).toEqual([]);
        });
    });

    it('sets title correctly', () => {
        const newTitle = 'My New Title';

        modalManager.init();

        modalManager.setTitle(newTitle);
        modalManager.getTitle().subscribe((_title: string) => {
            expect(_title).toEqual(newTitle);
        });
    });

    it('sets dismiss button visibility correctly', () => {
        modalManager.init();

        modalManager.setDismissButtonVisibility(false);
        modalManager.getIsDismissButtonVisible().subscribe((_isDismissButtonVisible: boolean) => {
            expect(_isDismissButtonVisible).toEqual(false);
        });
    });

    describe('Given the Save button is enabled - ' + 'When I click the Save Button - ', () => {
        it('And the button close - ' + 'Then the editor modal closes', () => {
            const button: ModalButtonOptions = {
                id: 'id1',
                label: 'label1',
                action: ModalButtonAction.Close
            };
            modalManager.init();

            modalManager.onButtonClicked(button);
            expect(modalRef.close).toHaveBeenCalled();
        });

        it('And the button action none - ' + 'Then the editor modal dismiss', () => {
            const button: ModalButtonOptions = {
                id: 'id1',
                label: 'label1',
                action: ModalButtonAction.None
            };
            modalManager.init();

            modalManager.onButtonClicked(button);
            expect(modalRef.close).not.toHaveBeenCalled();
        });
    });
});
