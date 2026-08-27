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
}