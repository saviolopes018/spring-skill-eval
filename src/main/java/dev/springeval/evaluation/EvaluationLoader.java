package dev.springeval.evaluation;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import tools.jackson.dataformat.yaml.YAMLMapper;

import java.nio.file.Path;

public class EvaluationLoader {

    private final YAMLMapper mapper;
    private final Validator validator;

    public EvaluationLoader(
            YAMLMapper mapper,
            Validator validator) {
        this.mapper = mapper;
        this.validator = validator;
    }

    public EvaluationSpec load(Path path) {

        var spec = mapper.readValue(
                path,
                EvaluationSpec.class);

        var violations = validator.validate(spec);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        return spec;
    }
}