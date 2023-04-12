package com.ghost.dev.network;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class NetworkTest implements HttpHandler {

    private final String name;

    public NetworkTest(String name) {
        this.name = name;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException{
        try(InputStream inStream = httpExchange.getRequestBody()) {

            String method = httpExchange.getRequestMethod();
            String response = "";
            switch (method) {
                case "GET" : response = getResponse();
            }
            System.out.println(new String(inStream.readAllBytes(), StandardCharsets.UTF_8));

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

    private String getResponse() {
        return name;
    }
}


