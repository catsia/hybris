import { TranslateService } from '@ngx-translate/core';
import {
    PERSONALIZATION_MODEL_STATUS_CODES,
    PersonalizationsmarteditUtils
} from 'personalizationcommons';
import { Observable } from 'rxjs';
import { L10nPipe } from 'smarteditcommons';

describe('PersonalizationsmarteditDateUtils', () => {
    let translateService: jasmine.SpyObj<TranslateService>;
    let l10nPipe: jasmine.SpyObj<L10nPipe>;
    let catalogService: jasmine.SpyObj<any>;

    // Service being tested
    let personalizationsmarteditUtils: PersonalizationsmarteditUtils;

    // === SETUP ===
    beforeEach(() => {
        translateService = jasmine.createSpyObj('translateService', ['instant']);
        l10nPipe = jasmine.createSpyObj<L10nPipe>('l10nPipe', ['transform']);
        l10nPipe.transform.and.callFake(
            (localizedMap) =>
                new Observable((subscriber) => {
                    // eslint-disable-next-line @typescript-eslint/dot-notation
                    subscriber.next(localizedMap['en']);
                })
        );
        catalogService = jasmine.createSpyObj('catalogService', ['debug']);

        translateService.instant.and.callFake(function (value: string): string {
            return value;
        });

        personalizationsmarteditUtils = new PersonalizationsmarteditUtils(
            translateService,
            l10nPipe,
            catalogService
        );
    });

    describe('pushToArrayIfValueExists', () => {
        it('should be defined', () => {
            expect(personalizationsmarteditUtils.pushToArrayIfValueExists).toBeDefined();
        });

        it('adds to array only values that exists', () => {
            // given
            const testArray: any = [];
            // when
            personalizationsmarteditUtils.pushToArrayIfValueExists(testArray, 'myKey', 'myValue');
            personalizationsmarteditUtils.pushToArrayIfValueExists(testArray, 'myKey2', null);
            personalizationsmarteditUtils.pushToArrayIfValueExists(testArray, 'myKey3', undefined);
            personalizationsmarteditUtils.pushToArrayIfValueExists(testArray, 'myKey4', 'myValue4');
            // then
            expect(testArray.length).toEqual(2);
        });
    });

    describe('getVariationCodes', () => {
        it('should be defined', () => {
            expect(personalizationsmarteditUtils.getVariationCodes).toBeDefined();
        });

        it('should return proper codes for variations', () => {
            // given
            const correctArray: any = [
                {
                    code: 'first',
                    value: 'next'
                },
                {
                    code: 'second',
                    value: 'none',
                    link: 'empty'
                }
            ];
            const incorrectArray: any = [
                {
                    link: 'my link',
                    value: 'next'
                },
                {
                    connection: 'second',
                    value: 'none',
                    link: 'empty'
                }
            ];
            // when
            const callForEmpty = personalizationsmarteditUtils.getVariationCodes([]);
            const callForCorrect = personalizationsmarteditUtils.getVariationCodes(correctArray);
            const callForIncorrect = personalizationsmarteditUtils.getVariationCodes(
                incorrectArray
            );
            // then
            expect(callForEmpty.length).toBe(0);

            expect(callForCorrect.length).toBe(2);
            expect(callForCorrect).toContain('first');
            expect(callForCorrect).toContain('second');

            expect(callForIncorrect.length).toBe(0);
        });
    });

    describe('getVariationKey', () => {
        it('should be defined', () => {
            expect(personalizationsmarteditUtils.getVariationKey).toBeDefined();
        });

        it('should return empty array if customization id is undefined', () => {
            expect(personalizationsmarteditUtils.getVariationKey(undefined, 'variations')).toEqual(
                []
            );
        });

        it('should return empty array if variations are undefined', () => {
            expect(
                personalizationsmarteditUtils.getVariationKey('customizationId', undefined)
            ).toEqual([]);
        });

        it('should return proper array if parameters are ok', () => {
            const mockVariations: any = [
                {
                    code: 'varTest1',
                    catalog: 'apparel-ukContentCatalog',
                    catalogVersion: 'Online'
                },
                {
                    code: 'varTest2',
                    catalog: 'apparel-ukContentCatalog',
                    catalogVersion: 'Online'
                }
            ];
            const mockResult: any = [
                {
                    variationCode: 'varTest1',
                    customizationCode: 'customizationId',
                    catalog: 'apparel-ukContentCatalog',
                    catalogVersion: 'Online'
                },
                {
                    variationCode: 'varTest2',
                    customizationCode: 'customizationId',
                    catalog: 'apparel-ukContentCatalog',
                    catalogVersion: 'Online'
                }
            ];

            expect(
                personalizationsmarteditUtils.getVariationKey('customizationId', mockVariations)
            ).toEqual(mockResult);
        });
    });

    describe('getSegmentTriggerForVariation', () => {
        it('should be defined', () => {
            expect(personalizationsmarteditUtils.getSegmentTriggerForVariation).toBeDefined();
        });

        it('should return empty object if no segmentTriggerData in variation', () => {
            const mockVariation: any = {
                code: 'test',
                triggers: []
            };
            expect(
                personalizationsmarteditUtils.getSegmentTriggerForVariation(mockVariation)
            ).toEqual({});
        });

        it('should return empty object if no segmentTriggerData in variation', () => {
            const mockSegment: any = {
                type: 'segmentTriggerData',
                name: 'testowy'
            };
            const mockTestTrigger: any = {
                type: 'myType',
                name: 'testName'
            };
            const mockVariation: any = {
                code: 'test',
                triggers: [mockTestTrigger, mockSegment]
            };
            expect(personalizationsmarteditUtils.getSegmentTriggerForVariation(mockVariation)).toBe(
                mockSegment
            );
        });
    });

    describe('isPersonalizationItemEnabled', () => {
        it('should be defined', () => {
            expect(personalizationsmarteditUtils.isPersonalizationItemEnabled).toBeDefined();
        });

        it('should return true if status for item is enabled', () => {
            const mockItem: any = {
                status: PERSONALIZATION_MODEL_STATUS_CODES.ENABLED
            };
            expect(personalizationsmarteditUtils.isPersonalizationItemEnabled(mockItem)).toBe(true);
        });

        it('should return false if status for item is disabled', () => {
            const mockItem: any = {
                status: PERSONALIZATION_MODEL_STATUS_CODES.DISABLED
            };
            expect(personalizationsmarteditUtils.isPersonalizationItemEnabled(mockItem)).toBe(
                false
            );
        });

        it('should return false if status for item is other than enabled', () => {
            const mockItem: any = {
                status: 'notImportantStatus'
            };
            expect(personalizationsmarteditUtils.isPersonalizationItemEnabled(mockItem)).toBe(
                false
            );
        });
    });

    describe('getEnablementTextForCustomization', () => {
        it('should be defined', () => {
            expect(personalizationsmarteditUtils.getEnablementTextForCustomization).toBeDefined();
        });

        it('should return key for disabled if customization is disabled', () => {
            const mockCustomization: any = {
                status: PERSONALIZATION_MODEL_STATUS_CODES.DISABLED
            };
            expect(
                personalizationsmarteditUtils.getEnablementTextForCustomization(
                    mockCustomization,
                    'myKey'
                )
            ).toBe('myKey.customization.disabled');
        });

        it('should return key for enabled if customization is enabled', () => {
            const mockCustomization: any = {
                status: PERSONALIZATION_MODEL_STATUS_CODES.ENABLED
            };
            expect(
                personalizationsmarteditUtils.getEnablementTextForCustomization(
                    mockCustomization,
                    'myKey'
                )
            ).toBe('myKey.customization.enabled');
        });
    });

    describe('getEnablementTextForVariation', () => {
        it('should be defined', () => {
            expect(personalizationsmarteditUtils.getEnablementTextForVariation).toBeDefined();
        });

        it('should return key for disabled if variation is disabled', () => {
            const mockVariation: any = {
                status: PERSONALIZATION_MODEL_STATUS_CODES.DISABLED
            };
            expect(
                personalizationsmarteditUtils.getEnablementTextForVariation(mockVariation, 'myKey')
            ).toBe('myKey.variation.disabled');
        });

        it('should return key for enabled if variation is enabled', () => {
            const mockVariation: any = {
                status: PERSONALIZATION_MODEL_STATUS_CODES.ENABLED
            };
            expect(
                personalizationsmarteditUtils.getEnablementTextForVariation(mockVariation, 'myKey')
            ).toBe('myKey.variation.enabled');
        });
    });

    describe('getEnablementActionTextForVariation', () => {
        it('should be defined', () => {
            expect(personalizationsmarteditUtils.getEnablementActionTextForVariation).toBeDefined();
        });

        it('should return key for disabled if variation is disabled', () => {
            const mockVariation: any = {
                status: PERSONALIZATION_MODEL_STATUS_CODES.DISABLED
            };
            expect(
                personalizationsmarteditUtils.getEnablementActionTextForVariation(
                    mockVariation,
                    'myKey'
                )
            ).toBe('myKey.variation.options.enable');
        });

        it('should return key for enabled if variation is enabled', () => {
            const mockVariation: any = {
                status: PERSONALIZATION_MODEL_STATUS_CODES.ENABLED
            };
            expect(
                personalizationsmarteditUtils.getEnablementActionTextForVariation(
                    mockVariation,
                    'myKey'
                )
            ).toBe('myKey.variation.options.disable');
        });
    });

    describe('getActivityStateForCustomization', () => {
        it('should be defined', () => {
            expect(personalizationsmarteditUtils.getActivityStateForCustomization).toBeDefined();
        });

        it('should return active if customization is active and dates are proper', () => {
            const mockCustomization: any = {
                status: PERSONALIZATION_MODEL_STATUS_CODES.ENABLED,
                enabledStartDate: '2010-10-10',
                enabledEndDate: '2110-11-11'
            };
            expect(
                personalizationsmarteditUtils.getActivityStateForCustomization(mockCustomization)
            ).toBe('perso__status--enabled');
        });

        it('should return ignore if customization is active but dates are incorrect', () => {
            const mockCustomization1: any = {
                status: PERSONALIZATION_MODEL_STATUS_CODES.ENABLED,
                enabledStartDate: '2110-10-10'
            };
            const mockCustomization2: any = {
                status: PERSONALIZATION_MODEL_STATUS_CODES.ENABLED,
                enabledEndDate: '2010-11-11'
            };
            expect(
                personalizationsmarteditUtils.getActivityStateForCustomization(mockCustomization1)
            ).toBe('perso__status--ignore');
            expect(
                personalizationsmarteditUtils.getActivityStateForCustomization(mockCustomization2)
            ).toBe('perso__status--ignore');
        });

        it('should return inactive if customization is disabled', () => {
            const mockCustomization: any = {
                status: PERSONALIZATION_MODEL_STATUS_CODES.DISABLED
            };
            expect(
                personalizationsmarteditUtils.getActivityStateForCustomization(mockCustomization)
            ).toBe('perso__status--disabled');
        });
    });

    describe('getActivityStateForVariation', () => {
        it('should be defined', () => {
            expect(personalizationsmarteditUtils.getActivityStateForVariation).toBeDefined();
        });

        it('should return inactive if variation is disabled', () => {
            const mockCustomization: any = {};
            const mockVariation: any = {
                enabled: false
            };
            expect(
                personalizationsmarteditUtils.getActivityStateForVariation(
                    mockCustomization,
                    mockVariation
                )
            ).toBe('perso__status--disabled');
        });

        it('should return inactive if variation is enabled but customization is disabled', () => {
            const mockCustomization = {
                status: PERSONALIZATION_MODEL_STATUS_CODES.DISABLED
            };
            const mockVariation: any = {
                enabled: true
            };
            expect(
                personalizationsmarteditUtils.getActivityStateForVariation(
                    mockCustomization,
                    mockVariation
                )
            ).toBe('perso__status--disabled');
        });

        it('should return active if variation is enabled and customization is enabled and dates for customization are proper', () => {
            const mockCustomization = {
                status: PERSONALIZATION_MODEL_STATUS_CODES.ENABLED,
                enabledStartDate: '2010-10-10',
                enabledEndDate: '2110-11-11'
            };
            const mockVariation: any = {
                enabled: true
            };
            expect(
                personalizationsmarteditUtils.getActivityStateForVariation(
                    mockCustomization,
                    mockVariation
                )
            ).toBe('perso__status--enabled');
        });

        it('should return ignore if variation is enabled and customization is enabled but dates for customization are incorrect', () => {
            const mockCustomization1: any = {
                status: PERSONALIZATION_MODEL_STATUS_CODES.ENABLED,
                enabledStartDate: '2110-10-10'
            };
            const mockCustomization2: any = {
                status: PERSONALIZATION_MODEL_STATUS_CODES.ENABLED,
                enabledEndDate: '2010-11-11'
            };
            const mockVariation: any = {
                enabled: true
            };
            expect(
                personalizationsmarteditUtils.getActivityStateForVariation(
                    mockCustomization1,
                    mockVariation
                )
            ).toBe('perso__status--ignore');
            expect(
                personalizationsmarteditUtils.getActivityStateForVariation(
                    mockCustomization2,
                    mockVariation
                )
            ).toBe('perso__status--ignore');
        });
    });

    describe('getVisibleItems', () => {
        it('should be defined', () => {
            expect(personalizationsmarteditUtils.getVisibleItems).toBeDefined();
        });

        it('should return list with visible items', () => {
            const mockItems: any = [
                {
                    status: 'test'
                },
                {
                    status: 'DELETED'
                },
                {
                    status: 'DELETED'
                },
                {
                    status: 'VISIBLE'
                },
                {
                    status: 'test'
                }
            ];
            const visibleItems: any = personalizationsmarteditUtils.getVisibleItems(mockItems);
            expect(visibleItems.length).toBe(3);
        });
    });

    describe('getStatusesMapping', () => {
        it('should be defined', () => {
            expect(personalizationsmarteditUtils.getStatusesMapping).toBeDefined();
        });

        it('should return array with proper mapping', () => {
            expect(personalizationsmarteditUtils.getStatusesMapping().length).toBe(3);
        });
    });

    describe('getValidRank', () => {
        it('should be defined', () => {
            expect(personalizationsmarteditUtils.getValidRank).toBeDefined();
        });

        it('should return proper rank even if some variations are in deleted state', () => {
            // given
            const variation1: any = {
                status: 'DELETED',
                rank: 0
            };

            const variation2: any = {
                status: 'DELETED',
                rank: 1
            };

            const variation3: any = {
                status: 'e',
                rank: 2
            };

            const variation4: any = {
                status: 'e',
                rank: 3
            };

            const variation5: any = {
                status: 'e',
                rank: 4
            };

            const variation6: any = {
                status: 'DELETED',
                rank: 5
            };

            const variation7: any = {
                status: 'e',
                rank: 6
            };

            const variation8: any = {
                status: 'DELETED',
                rank: 7
            };

            const variation9: any = {
                status: 'DELETED',
                rank: 8
            };

            const variations: any = [
                variation1,
                variation2,
                variation3,
                variation4,
                variation5,
                variation6,
                variation7,
                variation8,
                variation9
            ];
            let to = 0;

            // move down
            to = personalizationsmarteditUtils.getValidRank(variations, variation1, 1);
            expect(to).toBe(2);

            to = personalizationsmarteditUtils.getValidRank(variations, variation2, 1);
            expect(to).toBe(2);

            to = personalizationsmarteditUtils.getValidRank(variations, variation3, 1);
            expect(to).toBe(3);

            to = personalizationsmarteditUtils.getValidRank(variations, variation4, 1);
            expect(to).toBe(4);

            to = personalizationsmarteditUtils.getValidRank(variations, variation5, 1);
            expect(to).toBe(6);

            to = personalizationsmarteditUtils.getValidRank(variations, variation6, 1);
            expect(to).toBe(6);

            to = personalizationsmarteditUtils.getValidRank(variations, variation7, 1);
            expect(to).toBe(8);

            to = personalizationsmarteditUtils.getValidRank(variations, variation8, 1);
            expect(to).toBe(8);

            to = personalizationsmarteditUtils.getValidRank(variations, variation9, 1);
            expect(to).toBe(8);

            // move up
            to = personalizationsmarteditUtils.getValidRank(variations, variation1, -1);
            expect(to).toBe(0);

            to = personalizationsmarteditUtils.getValidRank(variations, variation2, -1);
            expect(to).toBe(0);

            to = personalizationsmarteditUtils.getValidRank(variations, variation3, -1);
            expect(to).toBe(0);

            to = personalizationsmarteditUtils.getValidRank(variations, variation4, -1);
            expect(to).toBe(2);

            to = personalizationsmarteditUtils.getValidRank(variations, variation5, -1);
            expect(to).toBe(3);

            to = personalizationsmarteditUtils.getValidRank(variations, variation6, -1);
            expect(to).toBe(4);

            to = personalizationsmarteditUtils.getValidRank(variations, variation7, -1);
            expect(to).toBe(4);

            to = personalizationsmarteditUtils.getValidRank(variations, variation8, -1);
            expect(to).toBe(6);

            to = personalizationsmarteditUtils.getValidRank(variations, variation9, -1);
            expect(to).toBe(6);
        });
    });
});
