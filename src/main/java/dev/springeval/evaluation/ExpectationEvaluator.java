package dev.springeval.evaluation;

import java.util.List;

public class ExpectationEvaluator {

    public ExpectationResult evaluate(
            ExpectationSpec expectation,
            String output) {

        var missing = expectation.outputContains()
                .stream()
                .filter(expected -> !output.contains(expected))
                .toList();

        return new ExpectationResult(
                missing.isEmpty(),
                List.copyOf(missing));
    }
}