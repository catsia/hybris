/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundservices.facade.impl

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.integrationservices.service.IntegrationObjectService
import de.hybris.platform.integrationservices.util.TestApplicationContext
import de.hybris.platform.outboundservices.client.IntegrationRestTemplateFactory
import de.hybris.platform.outboundservices.decorator.DecoratorContextFactory
import de.hybris.platform.outboundservices.decorator.RequestDecoratorService
import de.hybris.platform.outboundservices.decorator.impl.DefaultOutboundMonitoringRequestDecorator
import de.hybris.platform.outboundservices.facade.OutboundBatchRequestPartDTO
import de.hybris.platform.outboundservices.facade.SyncParameters
import de.hybris.platform.outboundservices.service.MultiPartRequestGenerator
import de.hybris.platform.servicelayer.search.FlexibleSearchService
import de.hybris.platform.testframework.JUnitPlatformSpecification
import org.junit.Rule
import org.junit.Test
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestOperations
import rx.observers.TestSubscriber
import spock.lang.Shared

@UnitTest
class DefaultOutboundServiceFacadeUnitTest extends JUnitPlatformSpecification {
    private static final String ENDPOINT_URL = "http://my.consumed.destination/some/path"
    private static final String DESTINATION_ID = 'destination'
    private static final String IOI_CODE = 'integrationObjectItemCode'
    private static final String ITEM_TYPE = 'MyType'
    private static final String IO_CODE = 'integrationObjectCode'
    private static final def RESPONSE = new ResponseEntity(HttpStatus.ACCEPTED)
    private static final def DESTINATION = new ConsumedDestinationModel(id: DESTINATION_ID, url: ENDPOINT_URL)
    private static final def HTTP_ENTITY = new HttpEntity([:])
    private static final def BATCH_HTTP_ENTITY = new HttpEntity('batchBody')
    private static final String BATCH_URI = '/$batch'


    @Shared
    def itemModel = Stub(ItemModel) {
        getItemtype() >> ITEM_TYPE
    }
    @Shared
    def integrationObjectService = Stub IntegrationObjectService

    @Rule
    TestApplicationContext applicationContext = new TestApplicationContext()

    def flexibleSearchService = Stub(FlexibleSearchService) {
        getModelByExample(_ as ConsumedDestinationModel) >> DESTINATION
    }
    def restClient = Mock(RemoteSystemClient)
    def contextFactory = Stub(DecoratorContextFactory)
    def requestDecoratorService = Stub(RequestDecoratorService)
    def facade = new DefaultOutboundServiceFacade(contextFactory, restClient)
    def batchRequestGenerator = Stub(MultiPartRequestGenerator)

    def setup() {
        facade.flexibleSearchService = flexibleSearchService
        facade.setRequestDecoratorService(requestDecoratorService)
        facade.setMultiPartRequestGenerator(batchRequestGenerator)
    }

    @Test
    def 'deprecated constructor still works and is backwards compatible'() {
        given: 'a rest template factory is set'
        requestDecoratorService.createHttpEntity(_ as SyncParameters) >> HTTP_ENTITY
        def factory = Stub(IntegrationRestTemplateFactory) {
            create(DESTINATION) >> Stub(RestOperations) {
                postForEntity(ENDPOINT_URL, _, Map) >> RESPONSE
            }
        }
        def oldFacade = new DefaultOutboundServiceFacade(
                decorators: [],
                flexibleSearchService: flexibleSearchService,
                integrationObjectService: integrationObjectService,
                integrationRestTemplateFactory: factory,
                monitoringDecorator: Stub(DefaultOutboundMonitoringRequestDecorator),
                contextFactory: contextFactory,
                requestDecoratorService: requestDecoratorService)

        when:
        def probe = new TestSubscriber()
        oldFacade.send(itemModel, IO_CODE, DESTINATION_ID).subscribe probe

        then:
        probe.assertValue RESPONSE
    }

    @Test
    def "cannot be created with null #param"() {
        when:
        new DefaultOutboundServiceFacade(factory, client)

        then:
        def e = thrown IllegalArgumentException
        e.message == "$param cannot be null"

        where:
        param                     | factory                       | client
        'DecoratorContextFactory' | null                          | Stub(RemoteSystemClient)
        'RemoteSystemClient'      | Stub(DecoratorContextFactory) | null
    }

    @Test
    def "send throws exception when destination is #condition"() {
        when:
        facade.send(itemModel, IO_CODE, destination)

        then:
        def ex = thrown IllegalArgumentException
        ex.message == 'destination cannot be null or empty'

        where:
        condition | destination
        'empty'   | ""
        'null'    | null
    }

    @Test
    def 'facade completes with a correct response when no errors occur'() {
        given: 'decorator service returns a successful response'
        def parameters = parameters()
        requestDecoratorService.createHttpEntity(parameters) >> HTTP_ENTITY

        when: 'facade is called'
        def probe = new TestSubscriber()
        facade.send(parameters).subscribe probe

        then: 'a post request is made to the remote system'
        1 * restClient.post(DESTINATION, _ as HttpEntity, Map.class) >> RESPONSE

        and: 'the item is sent and the response is received'
        probe.assertValue RESPONSE
        probe.assertCompleted()
        probe.assertNoErrors()
    }

    @Test
    def 'rest client is not invoked when the decorator service crashes'() {
        given: 'a decorator that crashes'
        def exception = new RuntimeException('IGNORE - testing exception')
        requestDecoratorService.createHttpEntity(_ as SyncParameters) >> { throw exception }

        when: 'facade is called'
        def probe = new TestSubscriber()
        facade.send(parameters()).subscribe probe

        then: 'the rest template was not created'
        0 * restClient._
    }

    @Test
    def 'facade completes with a correct response when decorator service is not injected'() {
        given: 'decorator service returns is not injected'
        facade.requestDecoratorService = null
        and:
        def parameters = parameters(DESTINATION)
        and: 'decorator service is present in the application context'
        applicationContext.addBean 'requestDecoratorService', Stub(RequestDecoratorService) {
            createHttpEntity(parameters) >> HTTP_ENTITY
        }

        when: 'facade is called'
        def probe = new TestSubscriber()
        facade.send(parameters).subscribe probe

        then: 'a post request is made to the remote system'
        1 * restClient.post(DESTINATION, HTTP_ENTITY, Map.class) >> RESPONSE

        and: 'the item is sent and the response is received'
        probe.assertValue RESPONSE
        probe.assertCompleted()
        probe.assertNoErrors()
    }

    @Test
    def 'rest client is not invoked in batch process when the decorator service crashes'() {
        given: 'a decorator that crashes'
        def exceptionMessage = 'IGNORE - testing exception'
        def exception = new RuntimeException(exceptionMessage)
        requestDecoratorService.createHttpEntity(_ as SyncParameters) >> { throw exception }

        when:
        facade.sendBatch([parameters(), parameters()])

        then:
        def e = thrown(RuntimeException)
        e.getMessage() == exceptionMessage
    }

    @Test
    def 'rest client is invoked once in batch process with the consolidated request httpEntity'() {
        given: 'decorator creates a request'
        requestDecoratorService.createHttpEntity(_ as SyncParameters) >> HTTP_ENTITY
        and: 'batch converter consolidates requests successfuly'
        batchRequestGenerator.generate(_ as List<OutboundBatchRequestPartDTO>) >> BATCH_HTTP_ENTITY

        when: 'facade is called'
        def response = facade.sendBatch([parameters(), parameters()])

        then: 'the rest client is invoked one time with the batch request'
        1 * restClient.post(DESTINATION, BATCH_HTTP_ENTITY, String.class, BATCH_URI) >> RESPONSE

        and: 'the item is sent and the response is received'
        response == RESPONSE
    }

    def findIntegrationObjectItemWithException(def ex) {
        Stub(IntegrationObjectService) {
            findIntegrationObject(IOI_CODE) >> {
                throw ex
            }
        }
    }

    SyncParameters parameters(ConsumedDestinationModel dest = null) {
        Stub(SyncParameters) {
            getItem() >> itemModel
            getDestinationId() >> DESTINATION.id
            getIntegrationObjectCode() >> IO_CODE
            getDestination() >> dest
        }
    }
}
