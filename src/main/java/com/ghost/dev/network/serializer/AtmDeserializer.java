package com.ghost.dev.network.serializer;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.ghost.dev.atm.model.AtmData;
import com.ghost.dev.processor.config.EmptyDataProcessorConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class AtmDeserializer implements Deserialize<EmptyDataProcessorConfig, AtmData[]> {

    private static final JsonFactory jFactory = new JsonFactory();

    @Override
    public Request<EmptyDataProcessorConfig, AtmData[]> deserialize(InputStream inputStream) throws IOException {
        JsonParser jParser = jFactory.createParser(inputStream);

        List<AtmData> atmList = new ArrayList<>();

        while (jParser.nextToken() != JsonToken.END_ARRAY) {
            int region = -1;
            String requestType = null;
            int atmId = -1;

            while (jParser.nextToken() != JsonToken.END_OBJECT) {
                String fieldName = jParser.currentName();
                if ("region".equals(fieldName)) {
                    jParser.nextToken();
                    region = jParser.getIntValue();
                }
                if ("requestType".equals(fieldName)) {
                    jParser.nextToken();
                    requestType = jParser.getText();
                }
                if ("atmId".equals(fieldName)) {
                    jParser.nextToken();
                    atmId = jParser.getIntValue();
                }
            }

            atmList.add(new AtmData(region, requestType, atmId));

        }

        return new Request<>(new EmptyDataProcessorConfig(), atmList.toArray(AtmData[]::new));
    }
}
