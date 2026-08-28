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

        public double scorePercentage() {

                var evaluatedCases = passedCases() + failedCases();

                if (evaluatedCases == 0) {
                        return 0.0;
                }

                return passedCases() * 100.0
                                / evaluatedCases;
        }

}
