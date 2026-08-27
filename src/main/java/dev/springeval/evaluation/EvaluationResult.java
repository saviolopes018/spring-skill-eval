package dev.springeval.evaluation;

import java.util.List;

public record EvaluationResult(

                String evaluationName,
                List<CaseResult> cases

) {

        public int totalCases() {
                return cases.size();
        }

        public long passedCases() {
                return cases.stream()
                                .filter(caseResult -> caseResult.evaluationStatus() == CaseEvaluationStatus.PASSED)
                                .count();
        }

        public long failedCases() {
                return cases.stream()
                                .filter(caseResult -> caseResult.evaluationStatus() == CaseEvaluationStatus.FAILED)
                                .count();
        }

        public long notEvaluatedCases() {
                return cases.stream()
                                .filter(caseResult -> caseResult
                                                .evaluationStatus() == CaseEvaluationStatus.NOT_EVALUATED)
                                .count();
        }

}
