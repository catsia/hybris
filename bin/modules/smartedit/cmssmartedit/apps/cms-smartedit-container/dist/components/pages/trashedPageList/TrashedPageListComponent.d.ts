import { ChangeDetectorRef, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { DynamicPagedListApi, DynamicPagedListConfig, ICatalogService, IDropdownMenuItem, IUrlService, Pagination, SystemEventService, TypedMap } from 'smarteditcommons';
export declare class TrashedPageListComponent implements OnInit, OnDestroy {
    private catalogService;
    private route;
    private urlService;
    private systemEventService;
    private cdr;
    siteUID: string;
    catalogId: string;
    catalogVersion: string;
    catalogName: TypedMap<string>;
    uriContext: any;
    trashedPageListConfig: DynamicPagedListConfig;
    mask: string;
    dropdownItems: IDropdownMenuItem[];
    dynamicPagedListApi: DynamicPagedListApi;
    unsubscribeEventListener: () => void;
    count: number;
    private maskSubject$;
    private maskSubscription;
    constructor(catalogService: ICatalogService, route: ActivatedRoute, urlService: IUrlService, systemEventService: SystemEventService, cdr: ChangeDetectorRef);
    ngOnInit(): Promise<void>;
    ngOnDestroy(): void;
    onMaskChange(newValue: string): void;
    onPageItemsUpdate(pagination: Pagination): void;
    reset(): void;
    getApi($api: any): void;
    private initialize;
    private onContentCatalogUpdate;
    private setSiteParams;
    private setUriContext;
    private setTrashedListConfigBasis;
    private setCatalogName;
    private setTrashedListColumns;
}
