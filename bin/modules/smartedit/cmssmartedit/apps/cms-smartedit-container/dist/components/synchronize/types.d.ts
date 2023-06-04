import { IUriContext, ICMSPage } from 'smarteditcommons';
export interface PageSyncConditions {
    canSyncHomepage: boolean;
    pageHasUnavailableDependencies: boolean;
    pageHasSyncStatus: boolean;
    pageHasNoDepOrNoSyncStatus: boolean;
}
export interface PageSynchronizationPanelModalData {
    cmsPage: ICMSPage;
    uriContext: IUriContext;
}
