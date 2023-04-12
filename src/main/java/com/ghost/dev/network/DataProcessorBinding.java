package com.ghost.dev.network;

import com.ghost.dev.network.serializer.Deserialize;
import com.ghost.dev.network.serializer.Request;
import com.ghost.dev.network.serializer.Serializer;
import com.ghost.dev.processor.ArrayDataInputStream;
import com.ghost.dev.processor.DataProcessor;
import com.ghost.dev.processor.config.DataProcessorConfig;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class DataProcessorBinding<C extends DataProcessorConfig, T, E> implements HttpHandler {
    private final DataProcessor<C, T, List<E>> dataProcessor;
    private final Deserialize<C, T[]> deserializeData;
    private final Serializer<List<E>> serializer;

    public DataProcessorBinding(
            DataProcessor<C, T, List<E>> dataProcessor,
            Deserialize<C, T[]> deserializeData,
            Serializer<List<E>> serializer) {
        this.dataProcessor = dataProcessor;
        this.deserializeData = deserializeData;
        this.serializer = serializer;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();

        try(InputStream inStream = httpExchange.getRequestBody()) {
            Request<C, T[]> request = deserializeData.deserialize(inStream);
            List<E> result = dataProcessor.processData(
                    request.config,
                    new ArrayDataInputStream<>(request.data)
            );

            String response = serializer.serialize(result);

            Headers responseHeaders = httpExchange.getResponseHeaders();
            addResponseHeaders(responseHeaders);

            //Sending back response to the client
            httpExchange.sendResponseHeaders(200, response.length());
            OutputStream outStream = httpExchange.getResponseBody();
            outStream.write(response.getBytes());
            outStream.close();
        }

    }

    private void addResponseHeaders(Headers responseHeaders) {
        responseHeaders.add("Access-Control-Allow-Origin", "*");
        responseHeaders.add("Access-Control-Allow-Headers","origin, content-type, accept, authorization");
        responseHeaders.add("Access-Control-Allow-Credentials", "true");
        responseHeaders.add("Access-Control-Allow-Methods", "GET, POST");
    }
}
