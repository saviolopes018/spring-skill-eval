package dev.springeval.evaluation;

public class ExpectationEvaluator {

    public boolean evaluate(
            ExpectationSpec expectation,
            String output) {
        return expectation.outputContains()
                .stream()
                .allMatch(output::contains);
    }
}