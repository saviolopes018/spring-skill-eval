package dev.springeval.ai;

import dev.springeval.evaluation.JudgePromptBuilder;
import dev.springeval.evaluation.JudgeRequest;
import dev.springeval.evaluation.JudgeResult;
import dev.springeval.evaluation.JudgeSpec;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SpringAiSemanticJudgeTest {

    @Test
    void shouldEvaluateSemanticCriteriaUsingChatClient() {

        var chatClient = mock(
                ChatClient.class,
                RETURNS_DEEP_STUBS);

        var promptBuilder = new JudgePromptBuilder();

        var request = new JudgeRequest(
                new JudgeSpec(
                        "The response must correctly identify the null-safety problem."),
                "Review this Java code.",
                "The value can be null and may cause NullPointerException.");

        var prompt = promptBuilder.build(request);

        when(
                chatClient
                        .prompt(prompt)
                        .call()
                        .entity(JudgeResult.class))
                .thenReturn(
                        new JudgeResult(
                                true,
                                "The response correctly identifies the null-safety issue."));

        var judge = new SpringAiSemanticJudge(
                chatClient,
                promptBuilder);

        var result = judge.evaluate(request);

        assertThat(result.passed())
                .isTrue();

        assertThat(result.reasoning())
                .contains("null-safety");
    }
}