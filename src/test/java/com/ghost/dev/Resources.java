package com.ghost.dev;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ghost.dev.network.serializer.Deserialize;
import com.ghost.dev.network.serializer.Request;
import com.ghost.dev.processor.config.DataProcessorConfig;
import com.ghost.dev.processor.config.EmptyDataProcessorConfig;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;

public class Resources {

    public static final String ATM_ROOT = "/atm/";
    public static final String ATM_REQUEST_1 = ATM_ROOT + "example_1_request.json";
    public static final String ATM_RESPONSE_1 = ATM_ROOT + "example_1_response.json";
    public static final String ATM_REQUEST_2 = ATM_ROOT + "example_2_request.json";
    public static final String ATM_RESPONSE_2 = ATM_ROOT + "example_2_response.json";

    public static final String TRANSACTION_ROOT = "/transaction/";
    public static final String TRANSACTION_REQUEST_1 = TRANSACTION_ROOT + "example_request.json";
    public static final String TRANSACTION_RESPONSE_1 = TRANSACTION_ROOT + "example_response.json";

    public static final String GAME_ROOT = "/game/";
    public static final String GAME_REQUEST_1 = GAME_ROOT + "example_request.json";
    public static final String GAME_RESPONSE_1 = GAME_ROOT + "example_response.json";


    public <T> T[] loadArray(String fileName, Deserialize<EmptyDataProcessorConfig, T[]> deserializer) {
        try (InputStream is = getClass().getResourceAsStream(fileName)) {
            return deserializer.deserialize(is).data;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public <C extends DataProcessorConfig, T> Request<C, T[]> loadObject(String fileName, Deserialize<C, T[]> deserializer) {
        try (InputStream is = getClass().getResourceAsStream(fileName)) {
            return deserializer.deserialize(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> T loadObject(String fileName, Class<T> clazz, Class<?> view) {
        try (InputStream is = getClass().getResourceAsStream(fileName)) {
            ObjectMapper mapper = new ObjectMapper();

            if (view != null) {
                mapper.readerWithView(view);
            }

            return mapper.readValue(is, clazz);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
