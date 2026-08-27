package dev.springeval.evaluation;

import dev.springeval.engine.ExecutionStatus;

import java.time.Duration;

public record CaseResult(
        String caseId,
        ExecutionStatus status,
        String output,
        String error,
        Duration duration,
        ExpectationResult expectation) {

    public CaseEvaluationStatus evaluationStatus() {

        if (expectation == null) {
            return CaseEvaluationStatus.NOT_EVALUATED;
        }

        return expectation.passed()
                ? CaseEvaluationStatus.PASSED
                : CaseEvaluationStatus.FAILED;
    }
}