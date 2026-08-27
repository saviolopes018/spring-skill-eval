package dev.springeval.evaluation;

import java.util.List;

public record EvaluationSuite(

        EvaluationSpec spec,
        List<EvaluationCaseSpec> cases

) {

}
