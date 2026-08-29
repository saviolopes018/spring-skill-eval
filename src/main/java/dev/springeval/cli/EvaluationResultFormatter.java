package dev.springeval.cli;

import dev.springeval.engine.ExecutionStatus;
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

                        builder.append("  Execution: ")
                                        .append(caseResult.status())
                                        .append(System.lineSeparator());

                        builder.append("  Output: ")
                                        .append(caseResult.output())
                                        .append(System.lineSeparator());

                        if (caseResult.error() != null && !caseResult.error().isBlank()) {

                                var label = caseResult.status() == ExecutionStatus.SUCCESS
                                                ? "Stderr: "
                                                : "Error: ";

                                builder.append("  ")
                                                .append(label)
                                                .append(caseResult.error())
                                                .append(System.lineSeparator());
                        }

                        if (caseResult.judge() != null) {
                                builder.append("  Judge passed: ")
                                                .append(caseResult.judge().passed())
                                                .append(System.lineSeparator());

                                builder.append("  Judge reasoning: ")
                                                .append(caseResult.judge().reasoning())
                                                .append(System.lineSeparator());
                        }
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