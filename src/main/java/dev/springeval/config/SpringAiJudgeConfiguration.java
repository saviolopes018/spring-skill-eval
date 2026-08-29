package dev.springeval.config;

import dev.springeval.ai.SpringAiSemanticJudge;
import dev.springeval.evaluation.JudgePromptBuilder;
import dev.springeval.evaluation.SemanticJudge;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringAiJudgeConfiguration {

    @Bean
    @ConditionalOnProperty(name = "spring.ai.model.chat", havingValue = "openai")
    SemanticJudge semanticJudge(
            ChatClient.Builder chatClientBuilder,
            JudgePromptBuilder promptBuilder) {

        var chatClient = chatClientBuilder.build();

        return new SpringAiSemanticJudge(
                chatClient,
                promptBuilder);
    }
}