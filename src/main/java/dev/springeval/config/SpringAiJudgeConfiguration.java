package dev.springeval.config;

import dev.springeval.ai.SpringAiSemanticJudge;
import dev.springeval.evaluation.JudgePromptBuilder;
import dev.springeval.evaluation.SemanticJudge;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringAiJudgeConfiguration {

    @Bean
    @ConditionalOnBean(ChatClient.class)
    SemanticJudge semanticJudge(
            ChatClient chatClient,
            JudgePromptBuilder promptBuilder) {
        return new SpringAiSemanticJudge(
                chatClient,
                promptBuilder);
    }
}