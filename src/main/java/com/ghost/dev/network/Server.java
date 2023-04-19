package com.ghost.dev.network;

import com.ghost.dev.atm.AtmDataProcessor;
import com.ghost.dev.game.GreedClanDataProcessor;
import com.ghost.dev.json.JacksonStreamFactory;
import com.ghost.dev.json.JsonFactory;
import com.ghost.dev.transaction.TransactionProcessor;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class Server {

    public static final String ATM_ENDPOINT = "/atms/calculateOrder";
    public static final String TRANSACTION_ENDPOINT = "/transactions/report";
    public static final String GAME_ENDPOINT = "/onlinegame/calculate";

    public static final JsonFactory jsonFactory = new JacksonStreamFactory(new com.fasterxml.jackson.core.JsonFactory());

    public Server() {
    }

    private void openAtmEndpoint(HttpServer server) {
        HttpContext atmContext = server.createContext(ATM_ENDPOINT);

        atmContext.setHandler(new DataProcessorBinding<>(
                new AtmDataProcessor(),
                jsonFactory.atmDeserializer(),
                jsonFactory.atmSerializer())
        );
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
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 300);

        openAtmEndpoint(server);
        openTransactionEndpoint(server);
        openGameEndpoint(server);

        server.setExecutor(Executors.newFixedThreadPool(4));

        server.start();
    }

}
