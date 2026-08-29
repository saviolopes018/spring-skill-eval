package dev.springeval.config;

import dev.springeval.engine.AgentEngine;
import dev.springeval.engine.AgentExecutionResult;
import dev.springeval.engine.ExecutionStatus;
import dev.springeval.evaluation.CaseEvaluationStatus;
import dev.springeval.evaluation.CaseRunner;
import dev.springeval.evaluation.EvaluationCaseSpec;
import dev.springeval.evaluation.JudgeResult;
import dev.springeval.evaluation.JudgeSpec;
import dev.springeval.evaluation.SemanticJudge;
import dev.springeval.skill.Skill;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.nio.file.Path;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = "spring.ai.model.chat=none")
@Import(CaseRunnerSpringWiringTest.TestJudgeConfiguration.class)
class CaseRunnerSpringWiringTest {

        @TempDir
        Path workspace;

        @Autowired
        CaseRunner caseRunner;

        @Test
        void shouldWireAvailableSemanticJudgeIntoCaseRunner() {

                AgentEngine engine = request -> new AgentExecutionResult(
                                ExecutionStatus.SUCCESS,
                                "The value can be null.",
                                "",
                                Duration.ofMillis(10));

                var evaluationCase = new EvaluationCaseSpec(
                                "case-001",
                                "Null safety",
                                "Review this Java code",
                                null,
                                new JudgeSpec(
                                                "The response must identify the null-safety problem."));

                var skill = new Skill(
                                workspace.resolve("SKILL.md"),
                                "# Java Reviewer");

                var result = caseRunner.run(
                                evaluationCase,
                                skill,
                                engine,
                                workspace,
                                Duration.ofSeconds(30));

                assertThat(result.judge())
                                .isNotNull();

                assertThat(result.judge().passed())
                                .isTrue();

                assertThat(result.judge().reasoning())
                                .isEqualTo("evaluated by configured judge");

                assertThat(result.evaluationStatus())
                                .isEqualTo(CaseEvaluationStatus.PASSED);
        }

        @TestConfiguration
        static class TestJudgeConfiguration {

                @Bean
                SemanticJudge semanticJudge() {
                        return request -> new JudgeResult(
                                        true,
                                        "evaluated by configured judge");
                }
        }
}