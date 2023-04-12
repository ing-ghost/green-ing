package com.ghost.dev.atm;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ghost.dev.Resources;
import com.ghost.dev.atm.model.AtmData;
import com.ghost.dev.atm.model.AtmStatus;
import com.ghost.dev.atm.model.AtmView;
import com.ghost.dev.processor.ArrayDataInputStream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.ghost.dev.processor.DataProcessorExecutor.processData;

public class AtmDataProcessorTest {

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
            AtmData[] request = new Resources().loadArray(testRequest[i], AtmData.class, AtmView.Request.class);
            List<AtmData> expectedResponse = Arrays.asList(new Resources().loadArray(testResponse[i], AtmData.class, AtmView.Request.class));

            List<AtmData> response = processData(
                    new AtmDataProcessorStream(),
                    new ArrayDataInputStream<>(request),
                    input -> input
            );

            ObjectMapper mapper = new ObjectMapper();


            String normalView = mapper
                    .writerWithView(AtmView.Normal.class)
                    .writeValueAsString(response);

            List<AtmData> responseClean = Arrays.asList(mapper.readValue(normalView, AtmData[].class));

            Assertions.assertEquals(expectedResponse, responseClean);

        }
    }

    @Test
    void testStatic() {
        processData(
                new AtmDataProcessor(),
                new ArrayDataInputStream<>(
                        new AtmTestData().staticTestData()
                ),
                input -> input
        );
    }

    @Test
    void testCompareQueueVsList() {

        ArrayDataInputStream<AtmData> data = new ArrayDataInputStream<>(
                new AtmTestData().generateTestData(1000, 500, 1000)
        );

        List<AtmData> result1 = processData(
                new AtmDataProcessor(),
                data,
                input -> input
        );

        List<AtmData> result2 = processData(
                new AtmDataProcessorQueue(),
                data,
                input -> input
        );

        Assertions.assertEquals(result1, result2);
    }

    @Test
    void testDynamic() {
        for (int i = 0; i < 3; i++) {

            ArrayDataInputStream<AtmData> data = new ArrayDataInputStream<>(
                    new AtmTestData().generateTestData(500, 490, 500)
            );

            // priority queue - 400ms
            // simple solution - 180ms
            // stream solution - 70ms
            List<AtmData> result = processData(
                    new AtmDataProcessorStream(),
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

    int eventToIndex(String request) {
        return switch (request) {
            case AtmStatus.FAILURE_RESTART -> 0;
            case AtmStatus.PRIORITY -> 1;
            case AtmStatus.SIGNAL_LOW -> 2;
            case AtmStatus.STANDARD -> 3;
            default -> 4;
        };
    }
}
