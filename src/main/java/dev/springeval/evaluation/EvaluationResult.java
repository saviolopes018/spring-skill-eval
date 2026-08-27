package dev.springeval.evaluation;

import java.util.List;

public record EvaluationResult(

                String evaluationName,
                List<CaseResult> cases

) {

}
