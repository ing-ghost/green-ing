package com.ghost.dev.atm;

import com.ghost.dev.Resources;
import com.ghost.dev.atm.model.AtmData;
import com.ghost.dev.atm.model.AtmStatus;
import com.ghost.dev.json.JacksonStreamFactory;
import com.ghost.dev.json.JsonFactory;
import com.ghost.dev.processor.ArrayDataInputStream;
import com.ghost.dev.processor.config.EmptyDataProcessorConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.ghost.dev.processor.DataProcessorExecutor.processData;

public class AtmDataProcessorTest {

    private final JsonFactory jsonFactory = new JacksonStreamFactory(new com.fasterxml.jackson.core.JsonFactory());

    @Test
    void resourceTest() throws IOException {
        String[] testRequest = new String[]{
                Resources.ATM_REQUEST_1,
                Resources.ATM_REQUEST_2
        };

        String[] testResponse = new String[]{
                Resources.ATM_RESPONSE_1,
                Resources.ATM_RESPONSE_2
        };

        for (int i = 0; i < 2; i++) {
            AtmData[] request = new Resources().loadArray(testRequest[i], jsonFactory.atmDeserializer());
            List<AtmData> expectedResponse = Arrays.asList(new Resources().loadArray(testResponse[i], jsonFactory.atmDeserializer()));

            List<AtmData> response = processData(
                    new AtmDataProcessor(),
                    new EmptyDataProcessorConfig(),
                    new ArrayDataInputStream<>(request),
                    input -> input
            );

            List<AtmData> responseClean = Arrays.asList(
                    jsonFactory.atmDeserializer()
                            .deserialize(
                                    new ByteArrayInputStream(jsonFactory.atmSerializer().serialize(response).getBytes(StandardCharsets.UTF_8))
                            ).data
            );


            Assertions.assertEquals(expectedResponse, responseClean);

        }
    }

    @Test
    void testStatic() {
        processData(
                new AtmDataProcessor(),
                new EmptyDataProcessorConfig(),
                new ArrayDataInputStream<>(
                        new AtmTestData().staticTestData()
                ),
                input -> input
        );
    }

    @Test
    void testDynamic() {
        for (int i = 0; i < 3; i++) {

            ArrayDataInputStream<AtmData> data = new ArrayDataInputStream<>(
                    new AtmTestData().generateTestData(500, 490, 500)
            );

            List<AtmData> result = processData(
                    new AtmDataProcessor(),
                    new EmptyDataProcessorConfig(),
                    data,
                    input -> input
            );

            int region = result.get(0).region;
            Set<Integer> atmId = new HashSet<>();
            int requestPriority = eventToIndex(AtmStatus.FAILURE_RESTART);

            for (AtmData atmData : result) {
                Assertions.assertTrue(region <= atmData.region);

                if (region == atmData.region) {
                    Assertions.assertFalse(atmId.contains(atmData.atmId));
                    int currentRequestPriority = eventToIndex(atmData.requestType);
                    Assertions.assertTrue(requestPriority <= currentRequestPriority,
                            " " + requestPriority + " " + currentRequestPriority);
                    requestPriority = currentRequestPriority;
                } else {
                    region = atmData.region;
                    atmId.clear();
                    requestPriority = eventToIndex(AtmStatus.FAILURE_RESTART);
                }
                atmId.add(atmData.atmId);
            }
        }
    }

    int eventToIndex(int request) {
        return switch (request) {
            case AtmStatus.FAILURE_RESTART -> 0;
            case AtmStatus.PRIORITY -> 1;
            case AtmStatus.SIGNAL_LOW -> 2;
            case AtmStatus.STANDARD -> 3;
            default -> 4;
        };
    }
}
