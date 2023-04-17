package com.ghost.dev.processor.stream;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.ghost.dev.atm.model.AtmData;
import com.ghost.dev.game.model.ClanData;
import com.ghost.dev.network.serializer.Serializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class GameSerializer<E> implements Serializer<List<List<ClanData>>> {

    private final JsonFactory jFactory;

    public GameSerializer(JsonFactory jFactory) {
        this.jFactory = jFactory;
    }

    @Override
    public String serialize(List<List<ClanData>> result) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        JsonGenerator generator = jFactory
                .createGenerator(stream, JsonEncoding.UTF8);

        generator.writeStartArray();

        for(List<ClanData> groupList : result) {
            generator.writeStartArray();

            for(ClanData clanData: groupList) {
                generator.writeStartObject();

                generator.writeNumberField("numberOfPlayers", clanData.numberOfPlayers);
                generator.writeNumberField("points", clanData.points);

                generator.writeEndObject();
            }
            generator.writeEndArray();
        }

        generator.writeEndArray();
        stream.close();

        String out = stream.toString("UTF-8");
        generator.close();
        return out;
    }
}
