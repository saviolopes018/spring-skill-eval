package dev.springeval.cli;

import dev.springeval.engine.ExecutionStatus;
import dev.springeval.evaluation.CaseResult;
import dev.springeval.evaluation.EvaluationResult;
import dev.springeval.evaluation.ExpectationResult;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class EvaluationResultFormatterTest {

        @Test
        void shouldFormatEvaluationResultForTerminal() {

                var passed = new CaseResult(
                                "case-001",
                                ExecutionStatus.SUCCESS,
                                "ok",
                                "",
                                Duration.ofMillis(10),
                                new ExpectationResult(true, List.of()));

                var failed = new CaseResult(
                                "case-002",
                                ExecutionStatus.SUCCESS,
                                "wrong",
                                "",
                                Duration.ofMillis(20),
                                new ExpectationResult(
                                                false,
                                                List.of("expected value")));

                var result = new EvaluationResult(
                                "java-reviewer",
                                List.of(passed, failed));

                var formatter = new EvaluationResultFormatter();

                var output = formatter.format(result);

                assertThat(output)
                                .contains("java-reviewer");

                assertThat(output)
                                .contains("case-001");

                assertThat(output)
                                .contains("PASSED");

                assertThat(output)
                                .contains("case-002");

                assertThat(output)
                                .contains("FAILED");

                assertThat(output)
                                .contains("50.0");

                assertThat(output)
                                .contains("Status: FAILED");
        }

        @Test
        void shouldNotLabelStderrAsErrorWhenExecutionSucceeds() {

                var caseResult = new CaseResult(
                                "case-001",
                                ExecutionStatus.SUCCESS,
                                "agent response",
                                "Codex diagnostic output",
                                Duration.ofMillis(10),
                                new ExpectationResult(true, List.of()));

                var result = new EvaluationResult(
                                "java-reviewer",
                                List.of(caseResult));

                var formatter = new EvaluationResultFormatter();

                var output = formatter.format(result);

                assertThat(output)
                                .contains("Stderr:")
                                .contains("Codex diagnostic output")
                                .doesNotContain("Error:");
        }
}