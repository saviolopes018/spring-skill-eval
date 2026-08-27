package dev.springeval.evaluation;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ExpectationEvaluatorTest {

    @Test
    void shouldPassWhenOutputContainsAllExpectedValues() {

        var evaluator = new ExpectationEvaluator();

        var expectation = new ExpectationSpec(
                List.of(
                        "null-safety",
                        "problems"));

        var result = evaluator.evaluate(
                expectation,
                "I found null-safety problems in this code.");

        assertThat(result.passed())
                .isTrue();

        assertThat(result.missingOutputContains())
                .isEmpty();
    }

    @Test
    void shouldFailWhenOutputDoesNotContainAllExpectedValues() {

        var evaluator = new ExpectationEvaluator();

        var expectation = new ExpectationSpec(
                List.of(
                        "null-safety",
                        "transaction"));

        var result = evaluator.evaluate(
                expectation,
                "I found null-safety problems in this code.");

        assertThat(result.passed())
                .isFalse();

        assertThat(result.missingOutputContains())
                .containsExactly("transaction");
    }
}