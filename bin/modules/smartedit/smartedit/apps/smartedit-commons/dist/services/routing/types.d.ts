import { NavigationEnd, NavigationError, NavigationStart } from '@angular/router';
export declare enum NavigationEventSource {
    NG = "ng"
}
export declare type RouteNavigationEnd = NavigationEnd;
export declare type RouteNavigationStart = NavigationStart;
export declare type RouteNavigationError = NavigationError;
export interface RouteChangeEvent<T> {
    from: NavigationEventSource;
    url: string;
    routeData: T;
}
