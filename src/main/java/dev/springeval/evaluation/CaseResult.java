package dev.springeval.evaluation;

import java.time.Duration;

import dev.springeval.engine.ExecutionStatus;

public record CaseResult(

        String caseId,
        ExecutionStatus status,
        String output,
        String error,
        Duration duration

) {

}
