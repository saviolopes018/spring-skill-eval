package dev.springeval.evaluation;

import org.junit.jupiter.api.Test;

import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.dataformat.yaml.YAMLMapper;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class EvaluationCaseLoaderTest {

        private YAMLMapper yamlMapper() {
                return YAMLMapper.builder()
                                .propertyNamingStrategy(
                                                PropertyNamingStrategies.SNAKE_CASE)
                                .build();
        }

        @Test
        void shouldLoadEvaluationCaseFromYaml() {

                var loader = new EvaluationCaseLoader(
                                yamlMapper());

                var resource = getClass()
                                .getClassLoader()
                                .getResource("cases/null-safety.yaml");

                assertThat(resource).isNotNull();

                var evaluationCase = loader.load(
                                Path.of(resource.getPath()));

                assertThat(evaluationCase.id())
                                .isEqualTo("null-safety-001");

                assertThat(evaluationCase.name())
                                .isEqualTo("Detect null safety issue");

                assertThat(evaluationCase.prompt())
                                .contains("null-safety problems");

                assertThat(evaluationCase.expect())
                                .isNotNull();

                assertThat(evaluationCase.expect().outputContains())
                                .containsExactly(
                                                "null-safety",
                                                "problems");

                assertThat(evaluationCase.judge())
                                .isNotNull();

                assertThat(evaluationCase.judge().criteria())
                                .contains("null-safety problem");
        }
}