package com.trainer.models.api.completion;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class LogProbResult {
    private List<String> tokens;
    private List<Double> tokenLogprobs;
    private List<Map<String, Double>> topLogprobs;
    private List<Integer> textOffset;

}
