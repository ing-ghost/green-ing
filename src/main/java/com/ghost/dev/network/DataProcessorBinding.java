package com.ghost.dev.network;

import com.ghost.dev.network.serializer.Deserialize;
import com.ghost.dev.network.serializer.Request;
import com.ghost.dev.network.serializer.Serializer;
import com.ghost.dev.processor.ArrayDataInputStream;
import com.ghost.dev.processor.DataProcessor;
import com.ghost.dev.processor.DataProcessorExecutor;
import com.ghost.dev.processor.config.DataProcessorConfig;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class DataProcessorBinding<C extends DataProcessorConfig, T, E> implements HttpHandler {
    private final DataProcessor<C, T, List<E>> dataProcessor;
    private final Deserialize<C, T[]> deserializeData;
    private final Serializer<List<E>> serializer;

    private final DataProcessorExecutor<C, T, List<E>> executor;

    public DataProcessorBinding(
            DataProcessor<C, T, List<E>> dataProcessor,
            Deserialize<C, T[]> deserializeData,
            Serializer<List<E>> serializer) {
        this.dataProcessor = dataProcessor;
        this.deserializeData = deserializeData;
        this.serializer = serializer;
        this.executor = new DataProcessorExecutor<>(dataProcessor);
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();

        try(InputStream inStream = httpExchange.getRequestBody()) {
            long start = System.currentTimeMillis();
            Request<C, T[]> request = deserializeData.deserialize(new BufferedInputStream(inStream, 100 * 1024));

            List<E> result = executor.execute(
                    request.config,
                    new ArrayDataInputStream<>(request.data)
            );

            byte[] response = serializer.serialize(result);
            System.out.println("TOTAL: " + (System.currentTimeMillis() - start));

            Headers responseHeaders = httpExchange.getResponseHeaders();
            addResponseHeaders(responseHeaders);

            //Sending back response to the client
            httpExchange.sendResponseHeaders(200, response.length);
            OutputStream outStream = httpExchange.getResponseBody();
            outStream.write(response);
            outStream.close();
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

    private void addResponseHeaders(Headers responseHeaders) {
        responseHeaders.add("Access-Control-Allow-Origin", "localhost:8080");
        responseHeaders.add("Access-Control-Allow-Headers","origin, content-type, accept, authorization");
        responseHeaders.add("Access-Control-Allow-Credentials", "true");
        responseHeaders.add("Access-Control-Allow-Methods", "GET, POST");
    }
}
