package dev.springeval.evaluation;

import dev.springeval.engine.ExecutionStatus;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class EvaluationResultTest {

    @Test
    void shouldSummarizeCaseEvaluationResults() {

        var passed = new CaseResult(
                "case-001",
                ExecutionStatus.SUCCESS,
                "ok",
                "",
                Duration.ofMillis(10),
                new ExpectationResult(
                        true,
                        List.of()));

        var failed = new CaseResult(
                "case-002",
                ExecutionStatus.SUCCESS,
                "wrong",
                "",
                Duration.ofMillis(10),
                new ExpectationResult(
                        false,
                        List.of("expected value")));

        var notEvaluated = new CaseResult(
                "case-003",
                ExecutionStatus.SUCCESS,
                "output",
                "",
                Duration.ofMillis(10),
                null);

        var result = new EvaluationResult(
                "java-reviewer",
                List.of(
                        passed,
                        failed,
                        notEvaluated));

        assertThat(result.totalCases())
                .isEqualTo(3);

        assertThat(result.passedCases())
                .isEqualTo(1);

        assertThat(result.failedCases())
                .isEqualTo(1);

        assertThat(result.notEvaluatedCases())
                .isEqualTo(1);
    }
}