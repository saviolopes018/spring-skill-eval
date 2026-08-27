package dev.springeval.evaluation;

import dev.springeval.engine.AgentEngineFactory;
import dev.springeval.engine.ExecutionStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.file.Path;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class EvaluationExecutionTest {

        @TempDir
        Path workspace;

        @Autowired
        EvaluationSuiteLoader suiteLoader;

        @Autowired
        AgentEngineFactory engineFactory;

        @Autowired
        EvaluationRunner evaluationRunner;

        @Test
        void shouldExecuteEvaluationLoadedFromYaml() {

                var resource = getClass()
                                .getClassLoader()
                                .getResource("evaluations/runnable-eval.yaml");

                assertThat(resource).isNotNull();

                var suite = suiteLoader.load(
                                Path.of(resource.getPath()));

                var engine = engineFactory.create(
                                suite.spec().engine());

                var result = evaluationRunner.run(
                                suite.spec().name(),
                                suite.cases(),
                                engine,
                                workspace,
                                Duration.ofSeconds(30));

                assertThat(result.evaluationName())
                                .isEqualTo("java-reviewer-eval");

                assertThat(result.cases())
                                .hasSize(1);

                var caseResult = result.cases().getFirst();

                assertThat(caseResult.caseId())
                                .isEqualTo("null-safety-001");

                assertThat(caseResult.status())
                                .isEqualTo(ExecutionStatus.SUCCESS);

                assertThat(caseResult.output())
                                .contains("null-safety problems");
        }
}