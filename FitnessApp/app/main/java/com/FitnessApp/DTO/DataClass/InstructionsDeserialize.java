package com.FitnessApp.DTO.DataClass;

import com.FitnessApp.Model.Exercise.Steps;
import com.FitnessApp.Repository.Exercise.StepRepository;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class InstructionsDeserialize extends StdDeserializer<List<Steps>> {

    public InstructionsDeserialize(){
        this(null);
    }

    protected InstructionsDeserialize(Class<?> vc) {
        super(vc);
    }

    @Override
    public List<Steps> deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {
        JsonNode node = jp.getCodec().readTree(jp);

        List<Steps> result = new ArrayList<>();
        AtomicInteger step = new AtomicInteger();
        node.forEach(jsonNode -> {
            result.add(new Steps(step.getAndIncrement(),jsonNode.asText("")));
        });

        return result;
    }
}