package dev.springeval.engine;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ProcessAgentEngineTest {

        @TempDir
        Path workspace;

        @Test
        void shouldExecuteProcessAndCaptureStdout() {

                var engine = new ProcessAgentEngine();

                var request = new ExecutionRequest(
                                List.of(
                                                "sh",
                                                "-c",
                                                "printf 'hello skill eval'"),
                                workspace,
                                Duration.ofSeconds(5));

                var result = engine.execute(request);

                assertThat(result.status())
                                .isEqualTo(ExecutionStatus.SUCCESS);

                assertThat(result.exitCode())
                                .isZero();

                assertThat(result.stdout())
                                .isEqualTo("hello skill eval");

                assertThat(result.stderr())
                                .isEmpty();

                assertThat(result.duration())
                                .isPositive();
        }

        @Test
        void shouldReturnErrorWhenProcessCannotBeStarted() {

                var engine = new ProcessAgentEngine();

                var request = new ExecutionRequest(
                                List.of("this-command-definitely-does-not-exist"),
                                workspace,
                                Duration.ofSeconds(5));

                var result = engine.execute(request);

                assertThat(result.status())
                                .isEqualTo(ExecutionStatus.ERROR);

                assertThat(result.exitCode())
                                .isEqualTo(-1);

                assertThat(result.stderr())
                                .isNotBlank();

                assertThat(result.duration())
                                .isPositive();
        }

        @Test
        void shouldReturnFailedWhenProcessExitsWithNonZeroCode() {

                var engine = new ProcessAgentEngine();

                var request = new ExecutionRequest(
                                List.of(
                                                "sh",
                                                "-c",
                                                "printf 'something went wrong' >&2; exit 2"),
                                workspace,
                                Duration.ofSeconds(5));

                var result = engine.execute(request);

                assertThat(result.status())
                                .isEqualTo(ExecutionStatus.FAILED);

                assertThat(result.exitCode())
                                .isEqualTo(2);

                assertThat(result.stdout())
                                .isEmpty();

                assertThat(result.stderr())
                                .isEqualTo("something went wrong");

                assertThat(result.duration())
                                .isPositive();
        }

        @Test
        void shouldReturnTimedOutWhenProcessExceedsTimeout() {

                var engine = new ProcessAgentEngine();

                var request = new ExecutionRequest(
                                List.of(
                                                "sh",
                                                "-c",
                                                "sleep 2"),
                                workspace,
                                Duration.ofMillis(100));

                var result = engine.execute(request);

                assertThat(result.status())
                                .isEqualTo(ExecutionStatus.TIMED_OUT);

                assertThat(result.exitCode())
                                .isEqualTo(-1);

                assertThat(result.duration())
                                .isLessThan(Duration.ofSeconds(1));
        }

}