package com.ghost.dev;

import com.fasterxml.jackson.core.JsonFactory;
import com.ghost.dev.atm.AtmTestData;
import com.ghost.dev.atm.model.AtmData;
import com.ghost.dev.json.JacksonStreamFactory;
import com.ghost.dev.json.SerializationFactory;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class GenerateData {

    private final SerializationFactory serializationFactory = new JacksonStreamFactory(new JsonFactory());

    @Disabled
    @Test
    void generate() throws IOException {
        AtmData[] atmData = new AtmTestData().generateTestData(500, 100, 200);

        String out = new String(serializationFactory.atmSerializer().serialize(Arrays.asList(atmData)), StandardCharsets.UTF_8);

        System.out.println(out.length() / (1024 * 1024));

        try(FileOutputStream f = new FileOutputStream("request.json")) {
            f.write(out.getBytes(StandardCharsets.UTF_8));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
