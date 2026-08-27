package dev.springeval.engine;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ProcessAgentEngine implements AgentEngine {

        @Override
        public ExecutionResult execute(ExecutionRequest request) {

                var startedAt = Instant.now();

                var processBuilder = new ProcessBuilder(request.command())
                                .directory(request.workspace().toFile());

                try {

                        var process = processBuilder.start();

                        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {

                                var stdoutFuture = executor.submit(() -> new String(
                                                process.getInputStream().readAllBytes(),
                                                StandardCharsets.UTF_8));

                                var stderrFuture = executor.submit(() -> new String(
                                                process.getErrorStream().readAllBytes(),
                                                StandardCharsets.UTF_8));

                                var finished = process.waitFor(
                                                request.timeout().toMillis(),
                                                TimeUnit.MILLISECONDS);

                                if (!finished) {

                                        process.destroyForcibly();
                                        process.waitFor();

                                        return new ExecutionResult(
                                                        ExecutionStatus.TIMED_OUT,
                                                        -1,
                                                        stdoutFuture.get(),
                                                        stderrFuture.get(),
                                                        Duration.between(startedAt, Instant.now()));
                                }

                                var stdout = stdoutFuture.get();
                                var stderr = stderrFuture.get();

                                var exitCode = process.exitValue();

                                var status = exitCode == 0
                                                ? ExecutionStatus.SUCCESS
                                                : ExecutionStatus.FAILED;

                                return new ExecutionResult(
                                                status,
                                                exitCode,
                                                stdout,
                                                stderr,
                                                Duration.between(startedAt, Instant.now()));
                        }

                } catch (IOException exception) {

                        return new ExecutionResult(
                                        ExecutionStatus.ERROR,
                                        -1,
                                        "",
                                        exception.getMessage(),
                                        Duration.between(startedAt, Instant.now()));

                } catch (InterruptedException exception) {

                        Thread.currentThread().interrupt();

                        throw new IllegalStateException(
                                        "Process execution was interrupted",
                                        exception);

                } catch (ExecutionException exception) {

                        return new ExecutionResult(
                                        ExecutionStatus.ERROR,
                                        -1,
                                        "",
                                        exception.getCause().getMessage(),
                                        Duration.between(startedAt, Instant.now()));
                }
        }
}