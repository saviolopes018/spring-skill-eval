package dev.springeval.evaluation;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JudgePromptBuilderTest {

        @Test
        void shouldBuildSemanticEvaluationPrompt() {

                var builder = new JudgePromptBuilder();

                var request = new JudgeRequest(
                                new JudgeSpec(
                                                "The response must correctly identify the null-safety problem."),
                                "Review this Java code.",
                                "The value can be null and may cause NullPointerException.");

                var prompt = builder.build(request);

                assertThat(prompt)
                                .contains(
                                                "The response must correctly identify the null-safety problem.");

                assertThat(prompt)
                                .contains("Review this Java code.");

                assertThat(prompt)
                                .contains(
                                                "The value can be null and may cause NullPointerException.");
        }
}