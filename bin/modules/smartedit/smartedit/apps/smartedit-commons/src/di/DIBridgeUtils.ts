/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { SeConstructor } from './types';

/** @internal */
export class DIBridgeUtils {
    // below function will be removed after 'downgradeService' tag is removed
    downgradeService(name: string, serviceConstructor: SeConstructor, token?: any): void {
        // disable sonar issue
    }
}

export const diBridgeUtils = new DIBridgeUtils();
