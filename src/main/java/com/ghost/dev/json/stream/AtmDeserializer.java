package com.ghost.dev.json.stream;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.ghost.dev.atm.model.AtmData;
import com.ghost.dev.atm.model.AtmStatus;
import com.ghost.dev.network.serializer.Deserialize;
import com.ghost.dev.network.serializer.Request;
import com.ghost.dev.processor.config.EmptyDataProcessorConfig;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class AtmDeserializer implements Deserialize<EmptyDataProcessorConfig, AtmData[]> {

    private final JsonFactory jFactory;

    public AtmDeserializer(JsonFactory jFactory) {
        this.jFactory = jFactory;
    }

    @Override
    public Request<EmptyDataProcessorConfig, AtmData[]> deserialize(InputStream inputStream) throws IOException {
        JsonParser jParser = jFactory.createParser(inputStream);

        List<AtmData> atmList = new ArrayList<>();

        while (jParser.nextToken() != JsonToken.END_ARRAY) {
            int region = -1;
            int requestType = -1;
            int atmId = -1;

            while (jParser.nextToken() != JsonToken.END_OBJECT) {
                String fieldName = jParser.currentName();
                if ("region".equals(fieldName)) {
                    jParser.nextToken();
                    region = jParser.getIntValue();
                }
                if ("requestType".equals(fieldName)) {
                    jParser.nextToken();
                    requestType = AtmStatus.of(jParser.getText());
                }
                if ("atmId".equals(fieldName)) {
                    jParser.nextToken();
                    atmId = jParser.getIntValue();
                }
            }

            atmList.add(new AtmData(region, requestType, atmId));

        }

        jParser.close();

        return new Request<>(new EmptyDataProcessorConfig(), atmList.toArray(AtmData[]::new));
    }
}