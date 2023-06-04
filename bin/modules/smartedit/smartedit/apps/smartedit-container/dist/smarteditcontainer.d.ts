import { BootstrapPayload, TypedMap } from 'smarteditcommons';
declare global {
    interface InternalSmartedit {
        smartEditBootstrapped: TypedMap<boolean>;
    }
}
export declare const SmarteditContainerFactory: (bootstrapPayload: BootstrapPayload) => any;
