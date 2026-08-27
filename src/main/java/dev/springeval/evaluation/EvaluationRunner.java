package dev.springeval.evaluation;

import java.nio.file.Path;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class EvaluationRunner {

    private final CaseRunner caseRunner;

    public EvaluationRunner(CaseRunner caseRunner) {
        this.caseRunner = caseRunner;
    }

    public EvaluationResult run(
            String evaluationName,
            List<EvaluationCaseSpec> cases,
            Path workspace,
            Duration timeout) {

        var results = new ArrayList<CaseResult>();

        for (var evaluationCase : cases) {
            var result = caseRunner.run(
                    evaluationCase,
                    workspace,
                    timeout);

            results.add(result);
        }

        return new EvaluationResult(
                evaluationName,
                List.copyOf(results));
    }
}