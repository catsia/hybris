/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { Component, Inject } from '@angular/core';
import {
    CatalogDetailsItemData,
    CATALOG_DETAILS_ITEM_DATA,
    SeDowngradeComponent,
    UserTrackingService,
    USER_TRACKING_FUNCTIONALITY
} from 'smarteditcommons';

@SeDowngradeComponent()
@Component({
    selector: 'se-page-list-link',
    templateUrl: './PageListLinkComponent.html'
})
export class PageListLinkComponent {
    constructor(
        @Inject(CATALOG_DETAILS_ITEM_DATA) public catalogDetails: CatalogDetailsItemData,
        private userTrackingService: UserTrackingService
    ) {}

    public getLink(): string {
        const {
            siteId,
            catalog: { catalogId },
            catalogVersion: { version }
        } = this.catalogDetails;

        return `#/pages/${siteId}/${catalogId}/${version}`;
    }

    public onClick(): void {
        const {
            catalogVersion: { version: catalogVersion }
        } = this.catalogDetails;

        this.userTrackingService.trackingUserAction(
            USER_TRACKING_FUNCTIONALITY.NAVIGATION,
            catalogVersion + '- Pages'
        );
    }
}
