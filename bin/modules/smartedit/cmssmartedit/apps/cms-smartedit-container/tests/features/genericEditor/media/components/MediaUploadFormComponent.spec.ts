/*
 * Copyright (c) 2022 SAP SE or an SAP affiliate company. All rights reserved.
 */
import { ChangeDetectorRef, DebugElement, NO_ERRORS_SCHEMA, SimpleChange } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import {
    BrowserDynamicTestingModule,
    platformBrowserDynamicTesting
} from '@angular/platform-browser-dynamic/testing';
import { ButtonModule } from '@fundamental-ngx/core';
import { TranslateModule } from '@ngx-translate/core';
import { ICMSMedia } from 'cmscommons';
import { MediaUploadFormComponent } from 'cmssmarteditcontainer/components/genericEditor/media/components';
import {
    MediaBackendValidationHandler,
    MediaFolderService,
    MediaUploaderService,
    MediaUtilService
} from 'cmssmarteditcontainer/components/genericEditor/media/services';
import { FILE_VALIDATION_CONFIG, FileValidatorFactory } from 'smarteditcommons';
import { FakeMediaUploadFieldComponent } from '../mockComponents';

describe('MediaUploadFormComponent', () => {
    let component: MediaUploadFormComponent;
    let fixture: ComponentFixture<MediaUploadFormComponent>;
    let nativeElement: HTMLElement;

    const fileValidatorFactory = new FileValidatorFactory();
    const cdr = jasmine.createSpyObj<ChangeDetectorRef>('changeDetectorRef', ['detectChanges']);
    let mediaBackendValidationHandler: jasmine.SpyObj<MediaBackendValidationHandler>;
    let mediaUploaderService: jasmine.SpyObj<MediaUploaderService>;
    let mediaFolderService: jasmine.SpyObj<MediaFolderService>;
    let mediaUtilService: jasmine.SpyObj<MediaUtilService>;

    const validFileName = 'validFile';

    async function createMediaUploadFormComponentContext() {
        TestBed.resetTestEnvironment();
        TestBed.initTestEnvironment(BrowserDynamicTestingModule, platformBrowserDynamicTesting());

        await TestBed.configureTestingModule({
            imports: [TranslateModule.forRoot(), ButtonModule],
            declarations: [MediaUploadFormComponent, FakeMediaUploadFieldComponent],
            providers: [
                { provide: ChangeDetectorRef, useValue: cdr },
                { provide: FileValidatorFactory, useValue: fileValidatorFactory },
                { provide: MediaBackendValidationHandler, useValue: mediaBackendValidationHandler },
                { provide: MediaUploaderService, useValue: mediaUploaderService },
                { provide: MediaFolderService, useValue: mediaFolderService },
                { provide: MediaUtilService, useValue: mediaUtilService }
            ],
            schemas: [NO_ERRORS_SCHEMA]
        })
            .overrideComponent(MediaUploadFormComponent, {
                set: {
                    templateUrl:
                        '/base/src/components/genericEditor/media/components/mediaUploadForm/MediaUploadFormComponent.html',
                    styleUrls: [
                        '/base/src/components/genericEditor/media/components/mediaUploadForm/MediaUploadFormComponent.scss'
                    ]
                }
            })
            .compileComponents();

        fixture = TestBed.createComponent(MediaUploadFormComponent);
        component = fixture.componentInstance;

        component.image = new File([''], validFileName, { type: 'jpg' });

        fixture.detectChanges();
        nativeElement = fixture.debugElement.nativeElement;
    }

    beforeEach(async () => {
        mediaBackendValidationHandler = jasmine.createSpyObj<MediaBackendValidationHandler>(
            'mediaBackendValidationHandler',
            ['handleResponse']
        );
        mediaUploaderService = jasmine.createSpyObj<MediaUploaderService>('mediaUploaderService', [
            'uploadMedia'
        ]);
        mediaUtilService = jasmine.createSpyObj<MediaUtilService>('mediaUtilService', [
            'getAcceptedFileTypes'
        ]);
        await createMediaUploadFormComponentContext();
    });

    afterEach(() => {
        TestBed.resetTestingModule();
    });

    it('Should create MediaUploadForm Component correctly', () => {
        expect(component).toBeDefined();
    });

    it('GIVEN file is selected THEN it shows the file name in header', () => {
        expect(nativeElement.querySelector('.se-media-upload-form__file-name').innerHTML).toBe(
            validFileName
        );
    });

    it('GIVEN a Media structure type is present in the Generic Editor WHEN I select an invalid image THEN I expect to see the file errors displayed', async () => {
        component.image = new File([''], 'inValidFile', { type: 'html/text' });
        component.maxUploadFileSize = 10;
        component.acceptedFileTypes = ['jpg'];

        component.ngOnChanges({
            image: new SimpleChange(undefined, component.image, false)
        });

        await component.uploadMedia();

        expect(component.fieldErrors.length).toEqual(1);
        expect(component.fieldErrors[0].message).toEqual(
            FILE_VALIDATION_CONFIG.I18N_KEYS.FILE_TYPE_INVALID
        );
    });

    it('GIVEN a Media structure type is present in the Generic Editor WHEN I select a valid image THEN I expect to see the media upload form populated', async () => {
        component.image = new File([''], 'validImage', { type: 'jpg' });
        component.maxUploadFileSize = 10;
        component.acceptedFileTypes = ['jpg'];

        component.ngOnChanges({
            image: new SimpleChange(undefined, component.image, false)
        });
        await component.uploadMedia();

        const codeAttribute: DebugElement = fixture.debugElement.query(
            By.css('se-media-upload-field[ng-reflect-field-name="code"]')
        );
        const descriptionAttribute: DebugElement = fixture.debugElement.query(
            By.css('se-media-upload-field[ng-reflect-field-name="description"]')
        );
        const altTextAttribute: DebugElement = fixture.debugElement.query(
            By.css('se-media-upload-field[ng-reflect-field-name="alt-text"]')
        );

        expect(component.fieldErrors.length).toEqual(0);
        expect(codeAttribute).toBeTruthy();
        expect(codeAttribute.nativeElement.getAttribute('ng-reflect-field-value')).toEqual(
            'validImage'
        );
        expect(descriptionAttribute).toBeTruthy();
        expect(descriptionAttribute.nativeElement.getAttribute('ng-reflect-field-value')).toEqual(
            'validImage'
        );
        expect(altTextAttribute).toBeTruthy();
        expect(altTextAttribute.nativeElement.getAttribute('ng-reflect-field-value')).toEqual(
            'validImage'
        );
    });

    it(
        'GIVEN a Media structure type is present in the Generic Editor WHEN I select a valid image AND upload with a missing code' +
            ' THEN I expect to see a field error for code',
        async () => {
            component.imageParams = {
                code: '',
                description: 'missing code',
                altText: 'missing code'
            };
            await component.uploadMedia();

            expect(component.fieldErrors.length).toEqual(1);
            expect(component.fieldErrors[0].message).toEqual('se.uploaded.image.code.is.required');
            expect(component.fieldErrors[0].subject).toEqual('code');
        }
    );

    it('GIVEN image has changed AND it has value THEN it sets imageParams properly', () => {
        const name = 'changedFile';
        component.image = new File([''], name, { type: 'png' });
        component.ngOnChanges({
            image: new SimpleChange(undefined, component.image, false)
        });

        expect(component.imageParams).toEqual({
            code: name,
            description: name,
            altText: name
        });
    });

    it('onCancel should reset params AND emit', () => {
        const emitSpy = spyOn(component.onCancel, 'emit');
        component.cancel();

        expect(component.imageParams).toEqual(null);
        expect(component.fieldErrors).toEqual([]);
        expect(component.isUploading).toBe(false);

        expect(emitSpy).toHaveBeenCalled();
    });

    describe('uploadMedia', () => {
        beforeEach(() => {
            component.acceptedFileTypes = ['jpg', 'png'];
            const image = { type: 'jpg' } as File;
            component.image = image;
        });

        it('GIVEN no validation errors THEN it uploads the image', async () => {
            const uploadedMedia = { uuid: '1' } as ICMSMedia;
            mediaUploaderService.uploadMedia.and.returnValue(Promise.resolve(uploadedMedia));

            const onUploadSuccessEmitSpy = spyOn(component.onUploadSuccess, 'emit');
            const name = 'someName';

            component.imageParams = {
                code: name,
                description: name,
                altText: name
            };

            await component.uploadMedia();

            expect(mediaUploaderService.uploadMedia).toHaveBeenCalledWith({
                file: component.image,
                code: name,
                description: name,
                altText: name,
                folder: ''
            });

            // assert that params has been reset
            expect(component.imageParams).toBeNull();
            expect(component.fieldErrors).toEqual([]);
            expect(component.isUploading).toBe(false);

            expect(onUploadSuccessEmitSpy).toHaveBeenCalledWith('1');
            expect(component.isUploading).toBe(false);
        });

        it('GIVEN uploadMedia fails THEN it delegates error handling to mediaBackendValidationHandler', async () => {
            component.imageParams = {
                code: 'code'
            } as any;

            mediaUploaderService.uploadMedia.and.returnValue(Promise.reject());

            await component.uploadMedia();

            expect(mediaBackendValidationHandler.handleResponse).toHaveBeenCalled();
        });

        it('GIVEN code is not provided THEN it populates errors AND does not upload', async () => {
            component.imageParams = {
                code: '',
                description: 'someName',
                altText: 'someName'
            };

            await component.uploadMedia();

            expect(mediaUploaderService.uploadMedia).not.toHaveBeenCalled();
            expect(component.fieldErrors.length).toBe(1);
        });
    });

    describe('getErrorsForFieldByCode', () => {
        it('should filter errors on subject and get messages', () => {
            component.fieldErrors = [
                {
                    subject: 'code',
                    message: 'some code message'
                }
            ];
            expect(component.getErrorsForFieldByCode('code')).toEqual(['some code message']);
        });

        it('should not populate messages for unmatched subjects in errors', () => {
            component.fieldErrors = [
                {
                    subject: 'code',
                    message: 'some code message'
                }
            ];
            expect(component.getErrorsForFieldByCode('altText')).toEqual([]);
        });
    });
});
