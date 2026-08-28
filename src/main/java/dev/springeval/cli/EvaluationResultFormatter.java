package dev.springeval.cli;

import dev.springeval.evaluation.EvaluationResult;
import org.springframework.stereotype.Component;

@Component
public class EvaluationResultFormatter {

    public String format(EvaluationResult result) {

        var builder = new StringBuilder();

        builder.append("Evaluation: ")
                .append(result.evaluationName())
                .append(System.lineSeparator())
                .append(System.lineSeparator());

        for (var caseResult : result.cases()) {
            builder.append(caseResult.caseId())
                    .append(" - ")
                    .append(caseResult.evaluationStatus())
                    .append(System.lineSeparator());
        }

        builder.append(System.lineSeparator())
                .append("Passed: ")
                .append(result.passedCases())
                .append(System.lineSeparator())

                .append("Failed: ")
                .append(result.failedCases())
                .append(System.lineSeparator())

                .append("Not evaluated: ")
                .append(result.notEvaluatedCases())
                .append(System.lineSeparator())

                .append("Score: ")
                .append(result.scorePercentage())
                .append("%")
                .append(System.lineSeparator())

                .append("Status: ")
                .append(result.status());

        return builder.toString();
    }
}