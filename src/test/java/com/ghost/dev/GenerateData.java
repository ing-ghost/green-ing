package com.ghost.dev;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ghost.dev.atm.AtmTestData;
import com.ghost.dev.atm.model.AtmData;
import com.ghost.dev.atm.model.AtmView;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class GenerateData {

    @Disabled
    @Test
    void generate() throws JsonProcessingException {
        AtmData[] atmData = new AtmTestData().generateTestData(500, 100, 200);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writerWithView(AtmView.Request.class);
        String out = objectMapper.writeValueAsString(atmData);
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
