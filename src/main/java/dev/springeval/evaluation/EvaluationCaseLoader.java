package dev.springeval.evaluation;

import java.nio.file.Path;

import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.dataformat.yaml.YAMLMapper;

public class EvaluationCaseLoader {

    private final YAMLMapper mapper;

    public EvaluationCaseLoader() {
        this.mapper = YAMLMapper.builder()
                .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
                .build();
    }

    public EvaluationCaseSpec load(Path path) {
        return mapper.readValue(path, EvaluationCaseSpec.class);
    }

}
