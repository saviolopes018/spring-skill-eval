package dev.springeval.cli;

import dev.springeval.evaluation.EvaluationExecutionService;
import org.springframework.shell.core.command.annotation.Argument;
import org.springframework.shell.core.command.annotation.Command;
import org.springframework.shell.core.command.annotation.Option;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component
public class EvaluationCommand {

    private final EvaluationExecutionService executionService;
    private final EvaluationResultFormatter formatter;

    public EvaluationCommand(
            EvaluationExecutionService executionService,
            EvaluationResultFormatter formatter) {
        this.executionService = executionService;
        this.formatter = formatter;
    }

    @Command(name = "run", description = "Run a skill evaluation")
    public String run(
            @Argument(index = 0, description = "Path to the evaluation YAML file") Path evaluationPath,

            @Option(longName = "workspace", shortName = 'w', description = "Workspace used during agent execution", defaultValue = ".") Path workspace) {

        var result = executionService.execute(
                evaluationPath,
                workspace);

        return formatter.format(result);
    }
}