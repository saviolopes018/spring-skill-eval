package dev.springeval.cli;

import dev.springeval.evaluation.EvaluationExecutionService;

import java.nio.file.Path;

public class EvaluationCommand {

    private final EvaluationExecutionService executionService;
    private final EvaluationResultFormatter formatter;

    public EvaluationCommand(
            EvaluationExecutionService executionService,
            EvaluationResultFormatter formatter) {
        this.executionService = executionService;
        this.formatter = formatter;
    }

    public String run(
            Path evaluationPath,
            Path workspace) {

        var result = executionService.execute(
                evaluationPath,
                workspace);

        return formatter.format(result);
    }
}