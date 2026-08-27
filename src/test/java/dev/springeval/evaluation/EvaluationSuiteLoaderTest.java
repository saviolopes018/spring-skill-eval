package dev.springeval.evaluation;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class EvaluationSuiteLoaderTest {

    @Test
    void shouldLoadEvaluationAndReferencedCases() {

        var evaluationLoader = new EvaluationLoader();
        var caseLoader = new EvaluationCaseLoader();

        var loader = new EvaluationSuiteLoader(
                evaluationLoader,
                caseLoader);

        var resource = getClass()
                .getClassLoader()
                .getResource("evaluations/runnable-eval.yaml");

        assertThat(resource).isNotNull();

        var evaluationPath = Path.of(resource.getPath());

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