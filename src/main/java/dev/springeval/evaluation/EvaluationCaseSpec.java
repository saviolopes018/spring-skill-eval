package dev.springeval.evaluation;

public record EvaluationCaseSpec(

                String id,
                String name,
                String prompt,
                ExpectationSpec expect

) {

        public EvaluationCaseSpec(
                        String id,
                        String name,
                        String prompt) {
                this(
                                id,
                                name,
                                prompt,
                                null);
        }

}
