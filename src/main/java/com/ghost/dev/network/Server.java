package com.ghost.dev.network;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ghost.dev.atm.AtmDataProcessor;
import com.ghost.dev.atm.model.AtmData;
import com.ghost.dev.atm.model.AtmView;
import com.ghost.dev.game.GreedClanDataProcessor;
import com.ghost.dev.network.serializer.ArrayDeserializer;
import com.ghost.dev.network.serializer.GameDeserializer;
import com.ghost.dev.network.serializer.ObjectMapperSerializer;
import com.ghost.dev.transaction.TransactionProcessor;
import com.ghost.dev.transaction.model.TransactionData;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Server {

    public static final String ATM_ENDPOINT = "/atms/calculateOrder";
    public static final String TRANSACTION_ENDPOINT = "/transactions/report";
    public static final String GAME_ENDPOINT = "/onlinegame/calculate";

    public Server() {
    }

    private void openAtmEndpoint(HttpServer server) {
        HttpContext atmContext = server.createContext(ATM_ENDPOINT);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writerWithView(AtmView.Normal.class);
        objectMapper.readerWithView(AtmView.Request.class);

        atmContext.setHandler(new DataProcessorBinding<>(
                new AtmDataProcessor(),
                new ArrayDeserializer<>(objectMapper, AtmData.class),
                new ObjectMapperSerializer<>(objectMapper))
        );
    }

    private void openTransactionEndpoint(HttpServer server) {
        HttpContext transactionContext = server.createContext(TRANSACTION_ENDPOINT);

        ObjectMapper objectMapper = new ObjectMapper();

        transactionContext.setHandler(new DataProcessorBinding<>(
                new TransactionProcessor(),
                new ArrayDeserializer<>(objectMapper, TransactionData.class),
                new ObjectMapperSerializer<>(objectMapper))
        );
    }

    private void openGameEndpoint(HttpServer server) {
        HttpContext transactionContext = server.createContext(GAME_ENDPOINT);

        ObjectMapper objectMapper = new ObjectMapper();

        transactionContext.setHandler(new DataProcessorBinding<>(
                new GreedClanDataProcessor(),
                new GameDeserializer(objectMapper),
                new ObjectMapperSerializer<>(objectMapper))
        );
    }

    public void start() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        openAtmEndpoint(server);
        openTransactionEndpoint(server);
        openGameEndpoint(server);

        server.start();
    }

}
