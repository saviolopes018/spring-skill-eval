package dev.springeval.evaluation;

public record EvaluationCaseSpec(
                String id,
                String name,
                String prompt,
                ExpectationSpec expect,
                JudgeSpec judge) {

        public EvaluationCaseSpec(
                        String id,
                        String name,
                        String prompt) {
                this(
                                id,
                                name,
                                prompt,
                                null,
                                null);
        }

        public EvaluationCaseSpec(
                        String id,
                        String name,
                        String prompt,
                        ExpectationSpec expect) {
                this(
                                id,
                                name,
                                prompt,
                                expect,
                                null);
        }
}