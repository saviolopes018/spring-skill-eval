package dev.springeval.evaluation;

import dev.springeval.engine.AgentEngineFactory;
import dev.springeval.engine.ExecutionStatus;
import dev.springeval.engine.ProcessRunner;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

class EvaluationExecutionTest {

        @TempDir
        Path workspace;

        @Test
        void shouldExecuteEvaluationLoadedFromYaml() {

                var evaluationLoader = new EvaluationLoader();
                var caseLoader = new EvaluationCaseLoader();

                var suiteLoader = new EvaluationSuiteLoader(
                                evaluationLoader,
                                caseLoader);

                var resource = getClass()
                                .getClassLoader()
                                .getResource("evaluations/runnable-eval.yaml");

                assertThat(resource).isNotNull();

                var suite = suiteLoader.load(
                                Path.of(resource.getPath()));

                var processRunner = new ProcessRunner();

                var engineFactory = new AgentEngineFactory(
                                processRunner);

                var engine = engineFactory.create(
                                suite.spec().engine());

                var caseRunner = new CaseRunner();

                var evaluationRunner = new EvaluationRunner(
                                caseRunner);

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