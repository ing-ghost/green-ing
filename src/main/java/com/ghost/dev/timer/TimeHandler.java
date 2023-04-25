package com.ghost.dev.timer;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class TimeHandler implements HttpHandler {

    private final TimeTracker timeTracker;

    public TimeHandler(TimeTracker timeTracker) {
        this.timeTracker = timeTracker;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        try(InputStream inStream = httpExchange.getRequestBody()) {
            String url = httpExchange.getRequestURI().toString();
            String response = switch (url) {
                case "/time/reset" -> {
                    timeTracker.reset();
                    yield "reset";
                }
                case "/time/summary" -> timeTracker.summary();
                default -> httpExchange.getRequestURI().toString();
            };

            Headers responseHeaders = httpExchange.getResponseHeaders();
            responseHeaders.add("Access-Control-Allow-Origin", "*");
            responseHeaders.add("Access-Control-Allow-Headers","origin, content-type, accept, authorization");
            responseHeaders.add("Access-Control-Allow-Credentials", "true");
            responseHeaders.add("Access-Control-Allow-Methods", "GET, POST");

            //Sending back response to the client
            httpExchange.sendResponseHeaders(200, response.length());
            OutputStream outStream = httpExchange.getResponseBody();
            outStream.write(response.getBytes());
            outStream.close();

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

}
