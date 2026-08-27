package dev.springeval.evaluation;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import dev.springeval.skill.SkillLoader;

public class EvaluationSuiteLoader {

    private final EvaluationLoader evaluationLoader;
    private final EvaluationCaseLoader caseLoader;
    private final SkillLoader skillLoader;

    public EvaluationSuiteLoader(
            EvaluationLoader evaluationLoader,
            EvaluationCaseLoader caseLoader,
            SkillLoader skillLoader) {
        this.evaluationLoader = evaluationLoader;
        this.caseLoader = caseLoader;
        this.skillLoader = skillLoader;
    }

    public EvaluationSuite load(Path evaluationPath) {

        var spec = evaluationLoader.load(evaluationPath);

        var baseDirectory = evaluationPath
                .toAbsolutePath()
                .getParent();

        var skillPath = baseDirectory
                .resolve(spec.skill().path())
                .normalize();

        var skill = skillLoader.load(skillPath);

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
                skill,
                List.copyOf(cases));
    }
}