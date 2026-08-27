package dev.springeval.evaluation;

import dev.springeval.engine.ExecutionStatus;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CaseResultTest {

    @Test
    void shouldMarkCaseAsFailedWhenExecutionFails() {

        var expectation = new ExpectationResult(
                true,
                List.of());

        var result = new CaseResult(
                "case-001",
                ExecutionStatus.FAILED,
                "expected output",
                "process failed",
                Duration.ofMillis(10),
                expectation);

        assertThat(result.evaluationStatus())
                .isEqualTo(CaseEvaluationStatus.FAILED);
    }
}