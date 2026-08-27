package dev.springeval.evaluation;

import dev.springeval.engine.AgentEngine;
import dev.springeval.engine.AgentExecutionResult;
import dev.springeval.engine.ExecutionStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class EvaluationRunnerTest {

        @TempDir
        Path workspace;

        @Test
        void shouldExecuteAllEvaluationCases() {

                AgentEngine engine = request -> new AgentExecutionResult(
                                ExecutionStatus.SUCCESS,
                                "completed: " + request.prompt(),
                                "",
                                Duration.ofMillis(10));

                var caseRunner = new CaseRunner();
                var runner = new EvaluationRunner(caseRunner);

                var cases = List.of(
                                new EvaluationCaseSpec(
                                                "case-001",
                                                "First case",
                                                "prompt one"),
                                new EvaluationCaseSpec(
                                                "case-002",
                                                "Second case",
                                                "prompt two"));

                var result = runner.run(
                                "java-reviewer-eval",
                                cases,
                                engine,
                                workspace,
                                Duration.ofSeconds(30));

                assertThat(result.evaluationName())
                                .isEqualTo("java-reviewer-eval");

                assertThat(result.cases())
                                .hasSize(2);

                assertThat(result.cases())
                                .extracting(CaseResult::caseId)
                                .containsExactly(
                                                "case-001",
                                                "case-002");

                assertThat(result.cases())
                                .extracting(CaseResult::output)
                                .containsExactly(
                                                "completed: prompt one",
                                                "completed: prompt two");
        }
}