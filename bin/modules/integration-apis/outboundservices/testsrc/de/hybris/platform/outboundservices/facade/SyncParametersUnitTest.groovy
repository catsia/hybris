/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company. All rights reserved.
 */
package de.hybris.platform.outboundservices.facade

import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.integrationservices.model.IntegrationObjectModel
import de.hybris.platform.outboundservices.enums.OutboundSource
import de.hybris.platform.outboundservices.event.EventType
import de.hybris.platform.outboundservices.event.impl.DefaultEventType
import de.hybris.platform.testframework.JUnitPlatformSpecification
import org.junit.Test
import spock.lang.Issue

@UnitTest
class SyncParametersUnitTest extends JUnitPlatformSpecification {
    private static final def CREATED_EVENT_TYPE = DefaultEventType.CREATED
    private static final def IO_CODE = 'TestObject'
    private static final def DESTINATION_ID = 'TestConsumedDest'
    private static final def INTEGRATION_KEY = 'key'
    private static final def CHANGE_ID = UUID.randomUUID().toString()
    private static final def INTEGRATION_MODEL = new IntegrationObjectModel(code: IO_CODE)
    private static final def ITEM = new ItemModel()
    private static final def DESTINATION_MODEL = new ConsumedDestinationModel(id: DESTINATION_ID)
    private static final def PARAMS1 = syncParameters()
    private static final def PARAMS2 = syncParameters2()
    private static final def UUID_PATTERN = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}"

    @Test
    def "SyncParameters is created with random changeId by the builder"() {
        expect:
        defaultsBuilder().build().getChangeId() != defaultsBuilder().build().getChangeId()
    }

    @Test
    def "two SyncParameters instances are equal when #condition"() {
        expect:
        PARAMS2 == syncParameters2(ITEM, ioCode, integrationObjectModel, destinationID, consumedDestinationModel, source)

        where:
        ioCode          | integrationObjectModel | destinationID   | consumedDestinationModel | source                      | condition
        IO_CODE         | INTEGRATION_MODEL      | DESTINATION_ID  | DESTINATION_MODEL        | OutboundSource.OUTBOUNDSYNC | 'all fields are equal'
        "anotherIOCode" | INTEGRATION_MODEL      | DESTINATION_ID  | DESTINATION_MODEL        | OutboundSource.OUTBOUNDSYNC | 'ioCode is different but ioModel is same'
        IO_CODE         | INTEGRATION_MODEL      | "AnotherDestID" | DESTINATION_MODEL        | OutboundSource.OUTBOUNDSYNC | 'destinationID is different but consumedDestinationModel is same'
        IO_CODE         | INTEGRATION_MODEL      | DESTINATION_ID  | DESTINATION_MODEL        | OutboundSource.OUTBOUNDSYNC | 'ChangeId is different'
    }

    @Test
    def "two SyncParameters instances are not equal when #condition"() {
        expect:
        PARAMS1 != syncParameters(item, outboundSource, eventType, integrationKey, changeId)

        where:
        item            | outboundSource                 | eventType                | integrationKey  | changeId  | condition
        new ItemModel() | OutboundSource.OUTBOUNDSYNC    | CREATED_EVENT_TYPE       | INTEGRATION_KEY | CHANGE_ID | 'item is different'
        ITEM            | OutboundSource.WEBHOOKSERVICES | CREATED_EVENT_TYPE       | INTEGRATION_KEY | CHANGE_ID | 'OutboundSource is different'
        ITEM            | OutboundSource.OUTBOUNDSYNC    | DefaultEventType.DELETED | INTEGRATION_KEY | CHANGE_ID | 'EventType is different'
        ITEM            | OutboundSource.OUTBOUNDSYNC    | CREATED_EVENT_TYPE       | "AnotherKey"    | CHANGE_ID | 'IntegrationKey is different'
    }

    @Test
    def "two SyncParameters instances are not equal when #conditions"() {
        expect:
        PARAMS2 != syncParameters2(ITEM, ioCode, integrationObjectModel, destinationID, consumedDestinationModel, source)

        where:
        ioCode  | integrationObjectModel       | destinationID  | consumedDestinationModel                                 | source                         | conditions
        IO_CODE | new IntegrationObjectModel() | DESTINATION_ID | DESTINATION_MODEL                                        | OutboundSource.OUTBOUNDSYNC    | 'IO is different'
        IO_CODE | INTEGRATION_MODEL            | DESTINATION_ID | new ConsumedDestinationModel(id: "anotherDestinationID") | OutboundSource.OUTBOUNDSYNC    | 'consumedDestinationModel is different'
        IO_CODE | INTEGRATION_MODEL            | DESTINATION_ID | DESTINATION_MODEL                                        | OutboundSource.WEBHOOKSERVICES | 'OutboundSource is different'
    }

    @Test
    def "two SyncParameters instances have same hash code when #condition"() {
        expect:
        PARAMS2.hashCode() == syncParameters2(ITEM, ioCode, integrationObjectModel, destinationID, consumedDestinationModel, source).hashCode()

        where:
        ioCode          | integrationObjectModel | destinationID   | consumedDestinationModel | source                      | condition
        IO_CODE         | INTEGRATION_MODEL      | DESTINATION_ID  | DESTINATION_MODEL        | OutboundSource.OUTBOUNDSYNC | 'all fields are equal'
        "anotherIOCode" | INTEGRATION_MODEL      | DESTINATION_ID  | DESTINATION_MODEL        | OutboundSource.OUTBOUNDSYNC | 'ioCode is different but ioModel is same'
        IO_CODE         | INTEGRATION_MODEL      | "AnotherDestID" | DESTINATION_MODEL        | OutboundSource.OUTBOUNDSYNC | 'destinationID is different but consumedDestinationModel is same'
    }

    @Test
    def "two SyncParameters instances have different hash code when #condition"() {
        expect:
        PARAMS2.hashCode() != syncParameters(item, outboundSource, eventType, integrationKey, changeId).hashCode()

        where:
        item            | outboundSource                 | eventType                | integrationKey  | changeId          | condition
        new ItemModel() | OutboundSource.OUTBOUNDSYNC    | CREATED_EVENT_TYPE       | INTEGRATION_KEY | CHANGE_ID         | 'item is different'
        ITEM            | OutboundSource.WEBHOOKSERVICES | CREATED_EVENT_TYPE       | INTEGRATION_KEY | CHANGE_ID         | 'OutboundSource is different'
        ITEM            | OutboundSource.OUTBOUNDSYNC    | DefaultEventType.DELETED | INTEGRATION_KEY | CHANGE_ID         | 'EventType is different'
        ITEM            | OutboundSource.OUTBOUNDSYNC    | CREATED_EVENT_TYPE       | "AnotherKey"    | CHANGE_ID         | 'IntegrationKey is different'
        ITEM            | OutboundSource.OUTBOUNDSYNC    | CREATED_EVENT_TYPE       | INTEGRATION_KEY | "AnotherChangeId" | 'ChangeId is different'
    }

    @Test
    def "two SyncParameters instances have different hash code when #conditions"() {
        expect:
        PARAMS2.hashCode() != syncParameters2(ITEM, ioCode, integrationObjectModel, destinationID, consumedDestinationModel, source).hashCode()

        where:
        ioCode  | integrationObjectModel       | destinationID  | consumedDestinationModel                                 | source                         | conditions
        IO_CODE | new IntegrationObjectModel() | DESTINATION_ID | DESTINATION_MODEL                                        | OutboundSource.OUTBOUNDSYNC    | 'IO is different'
        IO_CODE | INTEGRATION_MODEL            | DESTINATION_ID | new ConsumedDestinationModel(id: "anotherDestinationID") | OutboundSource.OUTBOUNDSYNC    | 'consumedDestinationModel is different'
        IO_CODE | INTEGRATION_MODEL            | DESTINATION_ID | DESTINATION_MODEL                                        | OutboundSource.WEBHOOKSERVICES | 'OutboundSource is different'
    }

    @Test
    def 'parameters correctly built when all fields are provided to the builder'() {
        given:
        def context = SyncParameters.syncParametersBuilder()
                .withIntegrationObjectCode(IO_CODE)
                .withItem(ITEM)
                .withDestinationId(DESTINATION_ID)
                .withDestination(DESTINATION_MODEL)
                .withIntegrationObject(INTEGRATION_MODEL)
                .withSource(OutboundSource.OUTBOUNDSYNC)
                .withEventType(CREATED_EVENT_TYPE)
                .withIntegrationKey(INTEGRATION_KEY)
                .withChangeId(CHANGE_ID)
                .build()

        expect:
        with(context) {
            integrationObjectCode == IO_CODE
            integrationObject == INTEGRATION_MODEL
            item == ITEM
            destinationId == DESTINATION_ID
            destination == DESTINATION_MODEL
            source == OutboundSource.OUTBOUNDSYNC
            eventType == CREATED_EVENT_TYPE
            integrationKey == INTEGRATION_KEY
            changeId == CHANGE_ID
        }
    }

    @Test
    def 'toString() contain information about all essential attributes'() {
        given:
        def params = defaultsBuilder().withChangeId(CHANGE_ID).build()

        expect:
        with(params.toString()) {
            contains "'${ITEM.toString()}'"
            contains "'$INTEGRATION_KEY'"
            contains "'$IO_CODE'"
            contains "'$DESTINATION_ID'"
            contains "'$OutboundSource.OUTBOUNDSYNC'"
            contains "'$CREATED_EVENT_TYPE.type'"
            contains "'$CHANGE_ID'"
        }

        params.getChangeId().matches(UUID_PATTERN)
    }

    @Test
    def '#field is populated when #model is provided'() {
        expect:
        with(context) {
            integrationObjectCode == IO_CODE
            item == ITEM
            destinationId == DESTINATION_ID
            source == OutboundSource.WEBHOOKSERVICES
        }

        where:
        field                   | model                    | context
        'integrationObjectCode' | 'integrationObjectModel' | params(ITEM, null, INTEGRATION_MODEL, DESTINATION_ID, DESTINATION_MODEL, OutboundSource.WEBHOOKSERVICES)
        'destinationId'         | 'destinationModel'       | params(ITEM, IO_CODE, INTEGRATION_MODEL, null, DESTINATION_MODEL, OutboundSource.WEBHOOKSERVICES)
    }

    @Test
    def 'when both integrationObjectCode and integrationObjectModel are provided, getIntegrationObjectCode returns the code of the integrationObjectModel'() {
        given:
        def parameters = params(ITEM, "alternativeID", INTEGRATION_MODEL, DESTINATION_ID, DESTINATION_MODEL, OutboundSource.WEBHOOKSERVICES)

        expect:
        parameters.getIntegrationObjectCode() == IO_CODE
    }

    @Test
    def 'when both destinationId and destinationModel are provided, getDestinationId returns the id of the destinationModel'() {
        given:
        def parameters = params(ITEM, IO_CODE, INTEGRATION_MODEL, "alternative_destination_id", DESTINATION_MODEL, OutboundSource.WEBHOOKSERVICES)

        expect:
        parameters.getDestinationId() == DESTINATION_ID
    }

    @Test
    def 'default to UNKNOWN source when null source is not provided'() {
        given:
        def context = SyncParameters.syncParametersBuilder()
                .withIntegrationObjectCode(IO_CODE)
                .withItem(ITEM)
                .withDestinationId(DESTINATION_ID)
                .build()

        expect:
        context.source == OutboundSource.UNKNOWN
    }

    @Test
    def "no exception is thrown when request is built with null #attr"() {
        expect:
        SyncParameters.syncParametersBuilder()
                .withItem(ITEM)
                .withDestination(destinationModel)
                .withDestinationId(destination)
                .withIntegrationObject(integrationModel)
                .withIntegrationObjectCode(io)
                .build()

        where:
        attr               | destination    | io      | destinationModel  | integrationModel  | changeId
        'destination'      | null           | IO_CODE | DESTINATION_MODEL | INTEGRATION_MODEL | CHANGE_ID
        'destinationModel' | DESTINATION_ID | IO_CODE | null              | INTEGRATION_MODEL | CHANGE_ID
        'io'               | DESTINATION_ID | null    | DESTINATION_MODEL | INTEGRATION_MODEL | CHANGE_ID
        'integrationModel' | DESTINATION_ID | IO_CODE | DESTINATION_MODEL | null              | CHANGE_ID
        'changeId'         | DESTINATION_ID | IO_CODE | DESTINATION_MODEL | INTEGRATION_MODEL | null
    }

    @Test
    def 'IllegalArgumentException is thrown when both item and integration key are null'() {
        when:
        defaultsBuilder()
                .withItem(null)
                .withIntegrationKey(null)
                .build()

        then:
        def e = thrown IllegalArgumentException
        e.message == 'At least one of item or integrationKey must be provided'
    }

    @Test
    def "parameters can be built with null #attr"() {
        expect:
        defaultsBuilder()
                .withItem(item)
                .withIntegrationKey(key)
                .build()

        where:
        attr             | item | key             | changeId
        'item'           | null | INTEGRATION_KEY | CHANGE_ID
        'integrationKey' | ITEM | null            | CHANGE_ID
        'changeId'       | ITEM | INTEGRATION_KEY | null
    }

    @Test
    def "can build a copy from another instance of parameters"() {
        given: 'pre-existing SyncParameters'
        def preExistingParameters = SyncParameters.syncParametersBuilder()
                .withIntegrationObjectCode(IO_CODE)
                .withItem(ITEM)
                .withDestination(DESTINATION_MODEL)
                .withDestinationId(DESTINATION_ID)
                .withIntegrationObject(INTEGRATION_MODEL)
                .withIntegrationObjectCode(IO_CODE)
                .withSource(OutboundSource.OUTBOUNDSYNC)
                .withEventType(CREATED_EVENT_TYPE)
                .withIntegrationKey(INTEGRATION_KEY)
                .build()

        when:
        def parameters = SyncParametersBuilder.from(preExistingParameters).build()

        then:
        with(preExistingParameters) {
            item == parameters.item
            destination == parameters.destination
            destinationId == parameters.destinationId
            integrationObject == parameters.integrationObject
            integrationObjectCode == parameters.integrationObjectCode
            source == parameters.source
            eventType == parameters.eventType
            integrationKey == parameters.integrationKey
        }
    }

    @Test
    @Issue("https://cxjira.sap.com/browse/IAPI-5212")
    def 'default to Unknown when event type is not provided'() {
        given:
        def context = SyncParameters.syncParametersBuilder()
                .withIntegrationObjectCode(IO_CODE)
                .withItem(ITEM)
                .withDestinationId(DESTINATION_ID)
                .build()

        expect:
        context.eventType == DefaultEventType.UNKNOWN
    }

    @Test
    def 'changeID is assigned with a UUID by default'() {
        given:
        def context = SyncParameters.syncParametersBuilder()
                .withIntegrationObjectCode(IO_CODE)
                .withItem(ITEM)
                .withDestinationId(DESTINATION_ID)
                .build()

        expect:
        context.getChangeId().matches(UUID_PATTERN)
    }

    @Test
    @Issue("https://cxjira.sap.com/browse/IAPI-5212")
    def "test build with specific event type"() {
        given:
        def context = SyncParameters.syncParametersBuilder()
                .withIntegrationObjectCode(IO_CODE)
                .withItem(ITEM)
                .withDestinationId(DESTINATION_ID)
                .withEventType(CREATED_EVENT_TYPE)
                .build()

        expect:
        context.eventType == CREATED_EVENT_TYPE
    }

    private static SyncParameters params(ItemModel item, String integrationObjCode, IntegrationObjectModel integrationObjectModel, String destinationId, ConsumedDestinationModel destinationModel, OutboundSource source, EventType event = DefaultEventType.UNKNOWN) {
        return SyncParameters.syncParametersBuilder()
                .withItem(item)
                .withIntegrationObjectCode(integrationObjCode)
                .withIntegrationObject(integrationObjectModel)
                .withDestinationId(destinationId)
                .withDestination(destinationModel)
                .withSource(source)
                .withEventType(event)
                .build()
    }

    private static SyncParametersBuilder defaultsBuilder() {
        SyncParameters.syncParametersBuilder()
                .withIntegrationObjectCode(IO_CODE)
                .withItem(ITEM)
                .withDestinationId(DESTINATION_ID)
                .withDestination(DESTINATION_MODEL)
                .withIntegrationObject(INTEGRATION_MODEL)
                .withSource(OutboundSource.OUTBOUNDSYNC)
                .withEventType(CREATED_EVENT_TYPE)
                .withIntegrationKey(INTEGRATION_KEY)
    }

    private static SyncParameters syncParameters(itemModel = ITEM,
                                                 source = OutboundSource.OUTBOUNDSYNC,
                                                 eventType = CREATED_EVENT_TYPE,
                                                 integrationKey = INTEGRATION_KEY,
                                                 changeId = CHANGE_ID) {
        return new SyncParameters(itemModel, source, eventType, integrationKey, changeId)
    }

    private static SyncParameters syncParameters2(final ItemModel itemModel = ITEM,
                                                  final String ioCode = IO_CODE,
                                                  final IntegrationObjectModel integrationObjectModel = INTEGRATION_MODEL,
                                                  final String destinationID = DESTINATION_ID,
                                                  final ConsumedDestinationModel consumedDestinationModel = DESTINATION_MODEL,
                                                  final OutboundSource source = OutboundSource.OUTBOUNDSYNC) {
        return new SyncParameters(itemModel, ioCode, integrationObjectModel, destinationID, consumedDestinationModel, source)
    }
}
