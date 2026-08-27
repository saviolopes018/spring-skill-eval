package dev.springeval.evaluation;

import java.nio.file.Path;

import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.dataformat.yaml.YAMLMapper;

public class EvaluationLoader {

    private final YAMLMapper mapper;

    public EvaluationLoader() {
        this.mapper = YAMLMapper.builder()
                .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
                .build();
    }

    public EvaluationSpec load(Path path) {
        return mapper.readValue(path, EvaluationSpec.class);
    }

}
