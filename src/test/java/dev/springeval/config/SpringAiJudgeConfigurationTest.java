package dev.springeval.config;

import dev.springeval.ai.SpringAiSemanticJudge;
import dev.springeval.evaluation.JudgePromptBuilder;
import dev.springeval.evaluation.SemanticJudge;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SpringAiJudgeConfigurationTest {

        private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
                        .withUserConfiguration(
                                        SpringAiJudgeConfiguration.class)
                        .withBean(
                                        JudgePromptBuilder.class,
                                        JudgePromptBuilder::new);

        @Test
        void shouldConfigureSemanticJudgeWhenChatClientBuilderIsAvailable() {

                var chatClient = mock(ChatClient.class);
                var builder = mock(ChatClient.Builder.class);

                when(builder.build())
                                .thenReturn(chatClient);

                contextRunner
                                .withBean(ChatClient.Builder.class, () -> builder)
                                .run(context -> {
                                        assertThat(context).hasSingleBean(SemanticJudge.class);
                                        assertThat(context.getBean(SemanticJudge.class))
                                                        .isInstanceOf(SpringAiSemanticJudge.class);
                                });
        }
}