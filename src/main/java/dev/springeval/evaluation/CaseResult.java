package dev.springeval.evaluation;

import dev.springeval.engine.ExecutionStatus;

import java.time.Duration;

public record CaseResult(
        String caseId,
        ExecutionStatus status,
        String output,
        String error,
        Duration duration,
        ExpectationResult expectation,
        JudgeResult judge) {

    public CaseResult(
            String caseId,
            ExecutionStatus status,
            String output,
            String error,
            Duration duration,
            ExpectationResult expectation) {
        this(
                caseId,
                status,
                output,
                error,
                duration,
                expectation,
                null);
    }

    public CaseEvaluationStatus evaluationStatus() {

        if (status != ExecutionStatus.SUCCESS) {
            return CaseEvaluationStatus.FAILED;
        }

        if (expectation != null && !expectation.passed()) {
            return CaseEvaluationStatus.FAILED;
        }

        if (judge != null && !judge.passed()) {
            return CaseEvaluationStatus.FAILED;
        }

        if (expectation == null && judge == null) {
            return CaseEvaluationStatus.NOT_EVALUATED;
        }

        return CaseEvaluationStatus.PASSED;
    }
}