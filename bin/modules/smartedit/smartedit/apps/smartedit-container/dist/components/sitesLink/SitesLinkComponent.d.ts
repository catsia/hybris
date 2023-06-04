import { SmarteditRoutingService, UserTrackingService } from 'smarteditcommons';
import { IframeManagerService } from 'smarteditcontainer/services/iframe/IframeManagerService';
import './sitesLink.scss';
export declare class SitesLinkComponent {
    private routingService;
    private readonly iframeManagerService;
    private readonly userTrackingService;
    cssClass: string;
    iconCssClass: string;
    shortcutLink: any;
    constructor(routingService: SmarteditRoutingService, iframeManagerService: IframeManagerService, userTrackingService: UserTrackingService);
    goToSites(): void;
}
