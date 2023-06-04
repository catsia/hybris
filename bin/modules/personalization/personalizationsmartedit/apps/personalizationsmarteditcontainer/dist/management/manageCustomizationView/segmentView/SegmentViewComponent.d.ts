/// <reference types="jquery" />
/// <reference types="eonasdan-bootstrap-datetimepicker" />
import { CdkDragDrop } from '@angular/cdk/drag-drop';
import { EventEmitter, OnInit } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { PersonalizationsmarteditMessageHandler, PersonalizationsmarteditUtils, Segment, TargetGroupState, Trigger, TriggerAction } from 'personalizationcommons';
import { FetchStrategy, SelectReset } from 'smarteditcommons';
import { PersonalizationsmarteditRestService } from '../../../service/PersonalizationsmarteditRestService';
import { TriggerService } from '../TriggerService';
import { SegmentItemPrinterComponent } from './SegmentItemPrinterComponent';
import { SegmentNodeComponent } from './SegmentNodeComponent';
export declare class SegmentViewComponent implements OnInit {
    private personalizationsmarteditRestService;
    private personalizationsmarteditMessageHandler;
    private triggerService;
    private personalizationsmarteditUtils;
    private translateService;
    private yjQuery;
    targetGroupState: TargetGroupState;
    expressionChange: EventEmitter<Trigger[]>;
    expression: Trigger[];
    segmentPrinterComponent: typeof SegmentItemPrinterComponent;
    nodeTemplate: typeof SegmentNodeComponent;
    elementToScroll: JQuery<HTMLElement>;
    scrollZoneVisible: boolean;
    actions: TriggerAction[];
    segments: Segment[];
    singleSegment: Segment;
    segmentFetchStrategy: FetchStrategy<Segment>;
    resetSelect: SelectReset;
    connectedDropListsIds: string[];
    private triggers;
    private triggerLookupMap;
    constructor(personalizationsmarteditRestService: PersonalizationsmarteditRestService, personalizationsmarteditMessageHandler: PersonalizationsmarteditMessageHandler, triggerService: TriggerService, personalizationsmarteditUtils: PersonalizationsmarteditUtils, translateService: TranslateService, yjQuery: JQueryStatic);
    ngOnInit(): void;
    get rootExpression(): Trigger;
    segmentSelectedEvent(itemCode: string): void;
    handleTreeUpdated(expression: Trigger[]): void;
    onDropHandler(event: CdkDragDrop<Trigger>): void;
    onDragStart(): void;
    loadSegmentItems(search: string, pageSize: number, currentPage: number): Promise<any>;
    private syncExpressionOnTriggerData;
    private getIdsRecursive;
    private isScrollZoneVisible;
}
