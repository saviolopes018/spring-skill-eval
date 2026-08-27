package dev.springeval.evaluation;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class EvaluationSuiteLoader {

    private final EvaluationLoader evaluationLoader;
    private final EvaluationCaseLoader caseLoader;

    public EvaluationSuiteLoader(
            EvaluationLoader evaluationLoader,
            EvaluationCaseLoader caseLoader) {
        this.evaluationLoader = evaluationLoader;
        this.caseLoader = caseLoader;
    }

    public EvaluationSuite load(Path evaluationPath) {

        var spec = evaluationLoader.load(evaluationPath);

        var baseDirectory = evaluationPath
                .toAbsolutePath()
                .getParent();

        var cases = new ArrayList<EvaluationCaseSpec>();

        for (var caseReference : spec.cases()) {

            var casePath = baseDirectory
                    .resolve(caseReference)
                    .normalize();

            var evaluationCase = caseLoader.load(casePath);

            cases.add(evaluationCase);
        }

        return new EvaluationSuite(
                spec,
                List.copyOf(cases));
    }
}