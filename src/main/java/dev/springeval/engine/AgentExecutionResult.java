package dev.springeval.engine;

import java.time.Duration;

public record AgentExecutionResult(

        ExecutionStatus status,
        String output,
        String error,
        Duration duration

) {

}
