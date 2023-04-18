package com.ghost.dev.network;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ghost.dev.atm.AtmDataProcessor;
import com.ghost.dev.atm.model.AtmView;
import com.ghost.dev.atm2.Atm2DataProcessor;
import com.ghost.dev.game.GreedClanDataProcessor;
import com.ghost.dev.processor.JsonFactory;
import com.ghost.dev.processor.factory.GsonFactory;
import com.ghost.dev.processor.factory.JacksonFactory;
import com.ghost.dev.processor.factory.StreamFactory;
import com.ghost.dev.processor.stream.Atm2Deserializer;
import com.ghost.dev.processor.stream.Atm2Serializer;
import com.ghost.dev.time.TimeHandler;
import com.ghost.dev.time.TimeTracker;
import com.ghost.dev.transaction.TransactionProcessor;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;


public class Server {

    public static final String ATM_ENDPOINT = "/atms/calculateOrder";
    public static final String ATM_2_ENDPOINT = "/atms/calculateOrder2";
    public static final String TRANSACTION_ENDPOINT = "/transactions/report";
    public static final String GAME_ENDPOINT = "/onlinegame/calculate";
    public static final String TIME_ENDPOINT = "/time";

//    public static final JsonFactory jsonFactory = new GsonFactory(new GsonBuilder().create());
//    public static final JsonFactory jsonFactory = jacksonFactory();
    public static final JsonFactory jsonFactory = streamFactory();

    private static final TimeTracker timeTracker = new TimeTracker();


    private static JsonFactory streamFactory() {
        return new StreamFactory(new com.fasterxml.jackson.core.JsonFactory());
    }

    private static JsonFactory jacksonFactory() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writerWithView(AtmView.Normal.class);
        objectMapper.readerWithView(AtmView.Request.class);

        return new JacksonFactory(objectMapper);
    }

    public Server() {
    }

    private void openAtmEndpoint(HttpServer server) {
        HttpContext atmContext = server.createContext(ATM_ENDPOINT);

        atmContext.setHandler(new DataProcessorBinding<>(
                timeTracker,
                new AtmDataProcessor(),
                jsonFactory.atmDeserializer(),
                jsonFactory.atmSerializer()
        ));
    }

    private void openAtm2Endpoint(HttpServer server) {
        HttpContext atmContext = server.createContext(ATM_2_ENDPOINT);

        com.fasterxml.jackson.core.JsonFactory factory = new com.fasterxml.jackson.core.JsonFactory();

        atmContext.setHandler(new DataProcessorBinding<>(
                timeTracker,
                new Atm2DataProcessor(),
                new Atm2Deserializer(factory),
                new Atm2Serializer(factory)
        ));
    }

    private void openTransactionEndpoint(HttpServer server) {
        HttpContext transactionContext = server.createContext(TRANSACTION_ENDPOINT);

        transactionContext.setHandler(new DataProcessorBinding<>(
                timeTracker,
                new TransactionProcessor(),
                jsonFactory.transactionDeserializer(),
                jsonFactory.transactionSerializer())
        );
    }

    private void openGameEndpoint(HttpServer server) {
        HttpContext transactionContext = server.createContext(GAME_ENDPOINT);

        transactionContext.setHandler(new DataProcessorBinding<>(
                timeTracker,
                new GreedClanDataProcessor(),
                jsonFactory.gameDeserializer(),
                jsonFactory.gameSerializer())
        );
    }

    private void openTimerEndpoint(HttpServer server) {
        HttpContext transactionContext = server.createContext(TIME_ENDPOINT);

        transactionContext.setHandler(
                new TimeHandler(timeTracker)
        );
    }

    public void start() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        openAtmEndpoint(server);
        openAtm2Endpoint(server);
        openTransactionEndpoint(server);
        openGameEndpoint(server);

        openTimerEndpoint(server);

        server.start();
    }

}
