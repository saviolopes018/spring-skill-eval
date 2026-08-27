package dev.springeval.evaluation;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class EvaluationCaseLoaderTest {

    @Test
    void shouldLoadEvaluationCaseFromYaml() {

        var loader = new EvaluationCaseLoader();

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
    }
}