package com.ghost.dev.network;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.ghost.dev.atm.AtmDataProcessor;
import com.ghost.dev.atm.model.AtmData;
import com.ghost.dev.atm.model.AtmView;
import com.ghost.dev.atm2.Atm2DataProcessor;
import com.ghost.dev.game.GreedClanDataProcessor;
import com.ghost.dev.processor.JsonFactory;
import com.ghost.dev.processor.factory.GsonFactory;
import com.ghost.dev.processor.factory.JacksonFactory;
import com.ghost.dev.processor.factory.StreamFactory;
import com.ghost.dev.processor.jackson.ArrayJacksonDeserializer;
import com.ghost.dev.processor.jackson.GameJacksonDeserializer;
import com.ghost.dev.processor.stream.Atm2Deserializer;
import com.ghost.dev.processor.stream.Atm2Serializer;
import com.ghost.dev.transaction.TransactionProcessor;
import com.ghost.dev.transaction.model.TransactionData;
import com.google.gson.Gson;
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

//    public static final JsonFactory jsonFactory = new GsonFactory(new GsonBuilder().create());
//    public static final JsonFactory jsonFactory = jacksonFactory();
    public static final JsonFactory jsonFactory = streamFactory();


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
                new AtmDataProcessor(),
                jsonFactory.atmDeserializer(),
                jsonFactory.atmSerializer()
        ));
    }

    private void openAtm2Endpoint(HttpServer server) {
        HttpContext atmContext = server.createContext(ATM_2_ENDPOINT);

        com.fasterxml.jackson.core.JsonFactory factory = new com.fasterxml.jackson.core.JsonFactory();

        atmContext.setHandler(new DataProcessorBinding<>(
                new Atm2DataProcessor(),
                new Atm2Deserializer(factory),
                new Atm2Serializer(factory)
        ));
    }

    private void openTransactionEndpoint(HttpServer server) {
        HttpContext transactionContext = server.createContext(TRANSACTION_ENDPOINT);

        transactionContext.setHandler(new DataProcessorBinding<>(
                new TransactionProcessor(),
                jsonFactory.transactionDeserializer(),
                jsonFactory.transactionSerializer())
        );
    }

    private void openGameEndpoint(HttpServer server) {
        HttpContext transactionContext = server.createContext(GAME_ENDPOINT);

        transactionContext.setHandler(new DataProcessorBinding<>(
                new GreedClanDataProcessor(),
                jsonFactory.gameDeserializer(),
                jsonFactory.gameSerializer())
        );
    }

    public void start() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        openAtmEndpoint(server);
        openAtm2Endpoint(server);
        openTransactionEndpoint(server);
        openGameEndpoint(server);

        server.start();
    }

}
