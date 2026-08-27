package dev.springeval.engine;

import java.time.Duration;

public record ExecutionResult(
                ExecutionStatus status,
                int exitCode,
                String stdout,
                String stderr,
                Duration duration) {
}