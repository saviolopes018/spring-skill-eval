package dev.springeval.engine;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ProcessRunnerTest {

        @TempDir
        Path workspace;

        @Test
        void shouldExecuteProcessAndCaptureStdout() {

                var runner = new ProcessRunner();

                var request = new ExecutionRequest(
                                List.of(
                                                "sh",
                                                "-c",
                                                "printf 'hello skill eval'"),
                                workspace,
                                Duration.ofSeconds(5));

                var result = runner.execute(request);

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

                var runner = new ProcessRunner();

                var request = new ExecutionRequest(
                                List.of("this-command-definitely-does-not-exist"),
                                workspace,
                                Duration.ofSeconds(5));

                var result = runner.execute(request);

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

                var runner = new ProcessRunner();

                var request = new ExecutionRequest(
                                List.of(
                                                "sh",
                                                "-c",
                                                "printf 'something went wrong' >&2; exit 2"),
                                workspace,
                                Duration.ofSeconds(5));

                var result = runner.execute(request);

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

                var runner = new ProcessRunner();

                var request = new ExecutionRequest(
                                List.of(
                                                "sh",
                                                "-c",
                                                "sleep 2"),
                                workspace,
                                Duration.ofMillis(100));

                var result = runner.execute(request);

                assertThat(result.status())
                                .isEqualTo(ExecutionStatus.TIMED_OUT);

                assertThat(result.exitCode())
                                .isEqualTo(-1);

                assertThat(result.duration())
                                .isLessThan(Duration.ofSeconds(1));
        }

        @Test
        void shouldConsumeLargeStdoutWithoutTimingOut() {

                var runner = new ProcessRunner();

                var request = new ExecutionRequest(
                                List.of(
                                                "sh",
                                                "-c",
                                                """
                                                                i=0
                                                                while [ $i -lt 100000 ]; do
                                                                  printf '0123456789abcdef\\n'
                                                                  i=$((i + 1))
                                                                done
                                                                """),
                                workspace,
                                Duration.ofSeconds(5));

                var result = runner.execute(request);

                assertThat(result.status())
                                .isEqualTo(ExecutionStatus.SUCCESS);

                assertThat(result.exitCode())
                                .isZero();

                assertThat(result.stdout())
                                .isNotEmpty();

                assertThat(result.stdout().length())
                                .isGreaterThan(1_000_000);
        }

}