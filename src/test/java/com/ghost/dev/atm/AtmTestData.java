package com.ghost.dev.atm;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ghost.dev.atm.model.AtmData;
import com.ghost.dev.atm.model.AtmView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.ghost.dev.atm.model.AtmStatus.FAILURE_RESTART;
import static com.ghost.dev.atm.model.AtmStatus.PRIORITY;
import static com.ghost.dev.atm.model.AtmStatus.SIGNAL_LOW;
import static com.ghost.dev.atm.model.AtmStatus.STANDARD;

public final class AtmTestData {

    public static final String REQUEST_1 = "example_1_request.json";
    public static final String RESPONSE_1 = "example_1_response.json";
    public static final String REQUEST_2 = "example_2_request.json";
    public static final String RESPONSE_2 = "example_2_response.json";

    public AtmData[] fromResource(String fileName) {
        try(InputStream is = getClass().getResourceAsStream("/atm/" + fileName)) {
            ObjectMapper mapper = new ObjectMapper();

            mapper.readerWithView(AtmView.Request.class);

            return mapper.readValue(is, AtmData[].class);
//            return gson.fromJson(new InputStreamReader(is), AtmData[].class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new AtmData[0];
    }
    
    public final AtmData[] staticTestData() {
        return new AtmData[] {
           new AtmData(4, STANDARD, 1),
                new AtmData(1, STANDARD, 1),
                new AtmData(2, STANDARD, 1),
                new AtmData(3, PRIORITY, 2),
                new AtmData(3, STANDARD, 1),
                new AtmData(2, SIGNAL_LOW, 1),
                new AtmData(5, STANDARD, 2),
                new AtmData(5, FAILURE_RESTART, 1)
        };
    }

    public final AtmData[] generateTestData(
            int regions, int atmMin, int atmMax
    ) {
        List<AtmData> data = new ArrayList<>();
        Random random = new Random();

        for(int i = 1; i<regions+1; i++) {
            int atmCount = random.nextInt(atmMin, atmMax + 1);
            for(int j = 0; j<atmCount; j++) {
                data.add(new AtmData(i, randomRequest(random), j));
            }
        }

        return data.toArray(new AtmData[0]);
    }

    private String randomRequest(Random random) {
        int index = random.nextInt(4);
        return switch (index) {
            case 0 -> STANDARD;
            case 1 -> PRIORITY;
            case 2 -> SIGNAL_LOW;
            case 3 -> FAILURE_RESTART;
            default -> STANDARD;
        };
    }
}
