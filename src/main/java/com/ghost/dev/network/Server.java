package com.ghost.dev.network;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.IntNode;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
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
import java.util.List;

class AtmDeserializer extends StdDeserializer<AtmData> {
    public AtmDeserializer() {
        this(null);
    }

    @Override
    public AtmData deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JacksonException {
        JsonNode node = jp.getCodec().readTree(jp);
        int id = node.get("region").intValue();
        String requestType = node.get("requestType").asText();
        int atmId = node.get("atmId").intValue();
        return new AtmData(id, requestType, atmId);
    }

    public AtmDeserializer(Class<AtmData> t) {
        super(t);
    }

}

class AtmSerializer extends StdSerializer<AtmData> {

    public AtmSerializer() {
        this(null);
    }

    public AtmSerializer(Class<AtmData> t) {
        super(t);
    }


    @Override
    public void serialize(AtmData value, JsonGenerator jgen, SerializerProvider provider) throws IOException {
        jgen.writeStartObject();
        jgen.writeNumberField("region", value.region);
        jgen.writeNumberField("atmId", value.atmId);
        jgen.writeEndObject();
    }
}

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


//        SimpleModule module = new SimpleModule();
//        module.addSerializer(AtmData.class, new AtmSerializer());
//        module.addDeserializer(AtmData.class, new AtmDeserializer());
//        objectMapper.registerModule(module);

        atmContext.setHandler(new DataProcessorBinding<>(
                new AtmDataProcessor(),
                new ArrayDeserializer<>(objectMapper, AtmData.class),
                                new ObjectMapperSerializer<>(objectMapper)
//                new com.ghost.dev.network.serializer.AtmDeserializer(),
//                new com.ghost.dev.network.serializer.AtmSerializer<List<AtmData>>()
        ));
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
