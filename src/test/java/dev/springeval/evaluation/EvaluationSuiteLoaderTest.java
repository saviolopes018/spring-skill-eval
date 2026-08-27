package dev.springeval.evaluation;

import jakarta.validation.Validation;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.dataformat.yaml.YAMLMapper;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class EvaluationSuiteLoaderTest {

        @Test
        void shouldLoadEvaluationAndReferencedCases() {

                var yamlMapper = YAMLMapper.builder()
                                .propertyNamingStrategy(
                                                PropertyNamingStrategies.SNAKE_CASE)
                                .build();

                var validator = Validation
                                .buildDefaultValidatorFactory()
                                .getValidator();

                var evaluationLoader = new EvaluationLoader(
                                yamlMapper,
                                validator);

                var caseLoader = new EvaluationCaseLoader(
                                yamlMapper);

                var loader = new EvaluationSuiteLoader(
                                evaluationLoader,
                                caseLoader);

                var resource = getClass()
                                .getClassLoader()
                                .getResource("evaluations/runnable-eval.yaml");

                assertThat(resource).isNotNull();

                var evaluationPath = Path.of(
                                resource.getPath());

                var suite = loader.load(evaluationPath);

                assertThat(suite.spec().name())
                                .isEqualTo("java-reviewer-eval");

                assertThat(suite.cases())
                                .hasSize(1);

                assertThat(suite.cases().getFirst().id())
                                .isEqualTo("null-safety-001");

                assertThat(suite.cases().getFirst().prompt())
                                .contains("null-safety problems");
        }
}