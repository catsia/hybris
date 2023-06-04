import { ElementRef, Injector } from '@angular/core';
import { SafeResourceUrl } from '@angular/platform-browser';
import { UpgradeModule } from '@angular/upgrade/static';
import { TranslateService } from '@ngx-translate/core';
import { AngularJSBootstrapIndicatorService, CrossFrameEventService } from 'smarteditcommons';
import { ThemeSwitchService } from 'smarteditcontainer/services/theme/ThemeSwitchService';
export declare class SmarteditcontainerComponent {
    private translateService;
    upgrade: UpgradeModule;
    private elementRef;
    private readonly bootstrapIndicator;
    private readonly themeSwitchService;
    private readonly crossFrameEventService;
    legacyAppName: string;
    cssUrl: SafeResourceUrl;
    cssCustomUrl: SafeResourceUrl;
    defaultTheme: boolean;
    constructor(translateService: TranslateService, injector: Injector, upgrade: UpgradeModule, elementRef: ElementRef, bootstrapIndicator: AngularJSBootstrapIndicatorService, themeSwitchService: ThemeSwitchService, crossFrameEventService: CrossFrameEventService);
    ngOnInit(): Promise<void>;
    ngAfterViewInit(): void;
    private setApplicationTitle;
    private get legacyAppNode();
    private isAppComponent;
    private isSmarteditLoader;
    private getThemeCss;
}
