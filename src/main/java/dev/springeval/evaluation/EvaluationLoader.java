package dev.springeval.evaluation;

import java.nio.file.Path;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.dataformat.yaml.YAMLMapper;

public class EvaluationLoader {

    private final YAMLMapper mapper;
    private final Validator validator;

    public EvaluationLoader() {
        this.mapper = YAMLMapper.builder()
                .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
                .build();

        this.validator = Validation
                .buildDefaultValidatorFactory()
                .getValidator();
    }

    public EvaluationSpec load(Path path) {

        var spec = mapper.readValue(path, EvaluationSpec.class);

        var violations = validator.validate(spec);

        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }

        return spec;
    }

}
