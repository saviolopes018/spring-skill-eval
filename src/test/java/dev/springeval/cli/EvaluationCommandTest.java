package dev.springeval.cli;

import dev.springeval.evaluation.EvaluationExecutionService;
import dev.springeval.evaluation.EvaluationResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class EvaluationCommandTest {

    @TempDir
    Path workspace;

    @Test
    void shouldExecuteEvaluationAndFormatResult() {

        var executionService = mock(EvaluationExecutionService.class);

        var formatter = mock(EvaluationResultFormatter.class);

        var evaluationPath = workspace.resolve("eval.yaml");

        var result = new EvaluationResult(
                "java-reviewer",
                List.of());

        when(
                executionService.execute(
                        evaluationPath,
                        workspace))
                .thenReturn(result);

        when(formatter.format(result))
                .thenReturn("formatted result");

        var command = new EvaluationCommand(
                executionService,
                formatter);

        var output = command.run(
                evaluationPath,
                workspace);

        assertThat(output)
                .isEqualTo("formatted result");
    }
}