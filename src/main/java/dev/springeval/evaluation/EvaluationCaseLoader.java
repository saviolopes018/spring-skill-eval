package dev.springeval.evaluation;

import tools.jackson.dataformat.yaml.YAMLMapper;

import java.nio.file.Path;

public class EvaluationCaseLoader {

    private final YAMLMapper mapper;

    public EvaluationCaseLoader(YAMLMapper mapper) {
        this.mapper = mapper;
    }

    public EvaluationCaseSpec load(Path path) {
        return mapper.readValue(
                path,
                EvaluationCaseSpec.class);
    }
}