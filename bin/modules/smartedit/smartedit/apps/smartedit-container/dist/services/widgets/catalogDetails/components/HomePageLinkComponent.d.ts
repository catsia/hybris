import { CatalogDetailsItemData, IExperienceService, UserTrackingService } from 'smarteditcommons';
export declare class HomePageLinkComponent {
    private experienceService;
    private readonly userTrackingService;
    private data;
    constructor(experienceService: IExperienceService, userTrackingService: UserTrackingService, data: CatalogDetailsItemData);
    onClick(): void;
}
