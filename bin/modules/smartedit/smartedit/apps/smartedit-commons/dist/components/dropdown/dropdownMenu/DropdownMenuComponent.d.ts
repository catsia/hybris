import { ChangeDetectorRef, ElementRef, EventEmitter, Injector, OnChanges, SimpleChanges } from '@angular/core';
import { Placement } from '../../popupOverlay';
import { IDropdownMenuItem } from './IDropdownMenuItem';
export declare class DropdownMenuComponent implements OnChanges {
    private cd;
    dropdownItems: IDropdownMenuItem[];
    selectedItem: any;
    selectedItemChange: EventEmitter<any>;
    placement: Placement;
    useProjectedAnchor: boolean;
    isOpen: boolean;
    additionalClasses: string[];
    isOpenChange: EventEmitter<boolean>;
    toggleMenuElement: ElementRef<HTMLDivElement>;
    clonedDropdownItems: IDropdownMenuItem[];
    dropdownMenuItemDefaultInjector: Injector;
    constructor(cd: ChangeDetectorRef);
    clickHandler(event: MouseEvent): void;
    ngOnChanges(changes: SimpleChanges): void;
    private emitIsOpenChange;
    private setDefaultComponentIfNeeded;
    private validateDropdownItem;
}
