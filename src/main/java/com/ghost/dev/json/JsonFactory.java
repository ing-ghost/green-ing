package com.ghost.dev.json;

import com.ghost.dev.atm.model.AtmData;
import com.ghost.dev.game.model.ClanData;
import com.ghost.dev.network.serializer.Deserialize;
import com.ghost.dev.network.serializer.Serializer;
import com.ghost.dev.processor.config.EmptyDataProcessorConfig;
import com.ghost.dev.processor.config.GameDataProcessorConfig;
import com.ghost.dev.transaction.model.AccountBalance;
import com.ghost.dev.transaction.model.TransactionData;

import java.util.List;

public interface JsonFactory {

    Serializer<List<AtmData>> atmSerializer();
    Serializer<List<AccountBalance>> transactionSerializer();
    Serializer<List<List<ClanData>>> gameSerializer();

    Deserialize<EmptyDataProcessorConfig, AtmData[]> atmDeserializer();
    Deserialize<EmptyDataProcessorConfig, TransactionData[]> transactionDeserializer();
    Deserialize<GameDataProcessorConfig, ClanData[]> gameDeserializer();

}
