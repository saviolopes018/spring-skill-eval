package dev.springeval.engine;

import dev.springeval.evaluation.ProcessEngineSpec;
import dev.springeval.skill.Skill;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.time.Duration;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ProcessAgentEngineTest {

        @TempDir
        Path workspace;

        @Test
        void shouldExecutePromptThroughConfiguredProcess() {

                var processRunner = new ProcessRunner();

                var engineSpec = new ProcessEngineSpec(
                                "printf",
                                List.of("%s"));

                var engine = new ProcessAgentEngine(
                                processRunner,
                                engineSpec);

                var request = new AgentExecutionRequest(
                                "hello agent",
                                workspace,
                                Duration.ofSeconds(5));

                var result = engine.execute(request);

                assertThat(result.status())
                                .isEqualTo(ExecutionStatus.SUCCESS);

                assertThat(result.output())
                                .isEqualTo("hello agent");

                assertThat(result.error())
                                .isEmpty();

                assertThat(result.duration())
                                .isPositive();
        }

        @Test
        void shouldExecuteProcessWithSkillInstructionsAndCasePrompt() {

                var processRunner = new ProcessRunner();

                var engineSpec = new ProcessEngineSpec(
                                "printf",
                                List.of("%s"));

                var engine = new ProcessAgentEngine(
                                processRunner,
                                engineSpec);

                var skill = new Skill(
                                workspace.resolve("SKILL.md"),
                                """
                                                # Java Reviewer

                                                Review Java code carefully.
                                                """);

                var request = new AgentExecutionRequest(
                                skill,
                                "Review this code for null-safety issues.",
                                workspace,
                                Duration.ofSeconds(5));

                var result = engine.execute(request);

                assertThat(result.status())
                                .isEqualTo(ExecutionStatus.SUCCESS);

                assertThat(result.output())
                                .contains("# Java Reviewer");

                assertThat(result.output())
                                .contains("Review Java code carefully.");

                assertThat(result.output())
                                .contains("Review this code for null-safety issues.");
        }
}