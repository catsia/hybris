/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import {
    BrowserDynamicTestingModule,
    platformBrowserDynamicTesting
} from '@angular/platform-browser-dynamic/testing';

import { TranslateModule } from '@ngx-translate/core';
import { Tab, TabsComponent, UserTrackingService } from 'smarteditcommons';
import { TestComponent } from './mockComponent/TestComponent';

describe('TabsComponent', () => {
    let tabsComponent: TabsComponent<any>;
    let fixture: ComponentFixture<TabsComponent<any>>;
    let nativeElement: HTMLElement;
    let userTrackingService: jasmine.SpyObj<UserTrackingService>;

    async function createTabsComponentContext() {
        TestBed.resetTestEnvironment();
        TestBed.initTestEnvironment(BrowserDynamicTestingModule, platformBrowserDynamicTesting());

        await TestBed.configureTestingModule({
            imports: [TranslateModule.forRoot()],
            declarations: [TabsComponent],
            providers: [{ provide: UserTrackingService, useValue: userTrackingService }],
            schemas: [NO_ERRORS_SCHEMA]
        })
            .overrideComponent(TabsComponent, {
                set: {
                    templateUrl: 'base/src/components/tabs/TabsComponent.html'
                }
            })
            .compileComponents();

        fixture = TestBed.createComponent(TabsComponent);
        tabsComponent = fixture.componentInstance;

        fixture.detectChanges();
        nativeElement = fixture.debugElement.nativeElement;
    }

    beforeEach(async () => {
        userTrackingService = jasmine.createSpyObj<UserTrackingService>('userTrackingService', [
            'trackingUserAction'
        ]);
        await createTabsComponentContext();
    });

    afterEach(() => {
        TestBed.resetTestingModule();
    });

    xit('Should create Tabs Component correctly', () => {
        expect(tabsComponent).toBeDefined();
    });

    xit(
        'Given the Editor modifies a tab - ' +
            'WHEN there has validation error - ' +
            'THEN an error will be display in the error tab',
        () => {
            const tab1 = {
                id: 'tab1',
                hasErrors: true,
                active: true,
                message: 'there has an error',
                title: 'tab1Header',
                component: TestComponent,
                disabled: false
            } as Tab;

            const tab2 = {
                id: 'tab2',
                hasErrors: false,
                active: true,
                message: '',
                title: 'tab2Header',
                component: TestComponent,
                disabled: true
            } as Tab;

            tabsComponent.tabsList = [tab1, tab2];

            tabsComponent.numTabsDisplayed = 2;
            tabsComponent.selectedTab = tab1;
            fixture.detectChanges();

            expect(nativeElement.querySelector('.sm-tab-error')).toBeTruthy();
            const tooltipBody: HTMLElement = nativeElement.querySelector('span[se-tooltip-body]');
            expect(tooltipBody).toBeTruthy();
            expect(tooltipBody.innerHTML).toContain(tab1.message);
        }
    );
});
