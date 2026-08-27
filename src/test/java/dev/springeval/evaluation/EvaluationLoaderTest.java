package dev.springeval.evaluation;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class EvaluationLoaderTest {

    @Test
    void shouldLoadEvaluationSpecFromYaml() {

        var loader = new EvaluationLoader();

        var resource = getClass()
                .getClassLoader()
                .getResource("evaluations/minimal-eval.yaml");

        assertThat(resource).isNotNull();

        var spec = loader.load(Path.of(resource.getPath()));

        assertThat(spec.schemaVersion()).isEqualTo("v1alpha1");
        assertThat(spec.name()).isEqualTo("java-reviewer-eval");

        assertThat(spec.skill().path())
                .isEqualTo("./skills/java-reviewer");

        assertThat(spec.engine().type())
                .isEqualTo("process");

        assertThat(spec.engine().command())
                .isEqualTo("codex");

        assertThat(spec.engine().args())
                .containsExactly("exec");

        assertThat(spec.cases())
                .containsExactly(
                        "./cases/null-safety.yaml",
                        "./cases/transaction.yaml");

        assertThat(spec.defaults().timeout())
                .isEqualTo("120s");
    }
}