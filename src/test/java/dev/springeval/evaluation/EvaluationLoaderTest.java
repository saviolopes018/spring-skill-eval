package dev.springeval.evaluation;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.dataformat.yaml.YAMLMapper;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class EvaluationLoaderTest {

        private YAMLMapper yamlMapper() {
                return YAMLMapper.builder()
                                .propertyNamingStrategy(
                                                PropertyNamingStrategies.SNAKE_CASE)
                                .build();
        }

        @Test
        void shouldLoadEvaluationSpecFromYaml() {

                var validator = Validation
                                .buildDefaultValidatorFactory()
                                .getValidator();

                var loader = new EvaluationLoader(
                                yamlMapper(),
                                validator);

                var resource = getClass()
                                .getClassLoader()
                                .getResource("evaluations/minimal-eval.yaml");

                assertThat(resource).isNotNull();

                var spec = loader.load(Path.of(resource.getPath()));

                assertThat(spec.schemaVersion()).isEqualTo("v1alpha1");
                assertThat(spec.name()).isEqualTo("java-reviewer-eval");

                assertThat(spec.skill().path())
                                .isEqualTo("./skills/java-reviewer");

                assertThat(spec.cases())
                                .containsExactly(
                                                "./cases/null-safety.yaml",
                                                "./cases/transaction.yaml");

                assertThat(spec.defaults().timeout())
                                .isEqualTo("120s");

                assertThat(spec.engine())
                                .isInstanceOf(ProcessEngineSpec.class);

                var engine = (ProcessEngineSpec) spec.engine();

                assertThat(engine.command()).isEqualTo("codex");
                assertThat(engine.args()).containsExactly("exec");
        }

        @Test
        void shouldRejectInvalidEvaluationSpec() {

                var validator = Validation
                                .buildDefaultValidatorFactory()
                                .getValidator();

                var loader = new EvaluationLoader(
                                yamlMapper(),
                                validator);

                var resource = getClass()
                                .getClassLoader()
                                .getResource("evaluations/invalid-eval.yaml");

                assertThat(resource).isNotNull();

                assertThatThrownBy(() -> loader.load(Path.of(resource.getPath())))
                                .isInstanceOfSatisfying(
                                                ConstraintViolationException.class,
                                                exception -> assertThat(exception.getConstraintViolations())
                                                                .extracting(violation -> violation.getPropertyPath()
                                                                                .toString())
                                                                .contains(
                                                                                "name",
                                                                                "skill.path",
                                                                                "engine.command",
                                                                                "cases",
                                                                                "defaults"));
        }

        @Test
        void shouldRejectUnknownEngineType() {

                var validator = Validation
                                .buildDefaultValidatorFactory()
                                .getValidator();

                var loader = new EvaluationLoader(
                                yamlMapper(),
                                validator);

                var resource = getClass()
                                .getClassLoader()
                                .getResource("evaluations/unknown-engine-eval.yaml");

                assertThat(resource).isNotNull();

                assertThatThrownBy(() -> loader.load(Path.of(resource.getPath())))
                                .hasMessageContaining("banana");
        }
}