package dev.springeval.ai;

import dev.springeval.evaluation.JudgePromptBuilder;
import dev.springeval.evaluation.JudgeRequest;
import dev.springeval.evaluation.JudgeResult;
import dev.springeval.evaluation.SemanticJudge;
import org.springframework.ai.chat.client.ChatClient;

public class SpringAiSemanticJudge implements SemanticJudge {

    private final ChatClient chatClient;
    private final JudgePromptBuilder promptBuilder;

    public SpringAiSemanticJudge(
            ChatClient chatClient,
            JudgePromptBuilder promptBuilder) {
        this.chatClient = chatClient;
        this.promptBuilder = promptBuilder;
    }

    @Override
    public JudgeResult evaluate(JudgeRequest request) {

        var prompt = promptBuilder.build(request);

        return chatClient
                .prompt(prompt)
                .call()
                .entity(JudgeResult.class);
    }
}