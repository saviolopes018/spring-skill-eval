package dev.springeval.engine;

import java.time.Duration;

public record ProcessExecutionResult(
        ExecutionStatus status,
        int exitCode,
        String stdout,
        String stderr,
        Duration duration) {
}