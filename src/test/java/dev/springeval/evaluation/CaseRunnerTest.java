package dev.springeval.evaluation;

import dev.springeval.engine.AgentEngine;
import dev.springeval.engine.AgentExecutionRequest;
import dev.springeval.engine.AgentExecutionResult;
import dev.springeval.engine.ExecutionStatus;
import dev.springeval.skill.Skill;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

class CaseRunnerTest {

        @TempDir
        Path workspace;

        @Test
        void shouldExecuteEvaluationCaseThroughAgentEngine() {

                AgentEngine engine = request -> new AgentExecutionResult(
                                ExecutionStatus.SUCCESS,
                                "review completed",
                                "",
                                Duration.ofMillis(50));

                var runner = new CaseRunner(
                                new ExpectationEvaluator());

                var evaluationCase = new EvaluationCaseSpec(
                                "null-safety-001",
                                "Detect null safety issue",
                                "Review this Java code");

                var skill = new Skill(
                                workspace.resolve("SKILL.md"),
                                "# Test Skill");

                var result = runner.run(
                                evaluationCase,
                                skill,
                                engine,
                                workspace,
                                Duration.ofSeconds(30));

                assertThat(result.caseId())
                                .isEqualTo("null-safety-001");

                assertThat(result.status())
                                .isEqualTo(ExecutionStatus.SUCCESS);

                assertThat(result.output())
                                .isEqualTo("review completed");
        }

        @Test
        void shouldPassCaseExecutionContextToAgentEngine() {

                var capturedRequest = new AtomicReference<AgentExecutionRequest>();

                AgentEngine engine = request -> {
                        capturedRequest.set(request);

                        return new AgentExecutionResult(
                                        ExecutionStatus.SUCCESS,
                                        "done",
                                        "",
                                        Duration.ofMillis(10));
                };

                var runner = new CaseRunner(
                                new ExpectationEvaluator());

                var evaluationCase = new EvaluationCaseSpec(
                                "case-001",
                                "Example case",
                                "Analyze this code");

                var timeout = Duration.ofSeconds(45);

                var skill = new Skill(
                                workspace.resolve("SKILL.md"),
                                """
                                                # Java Reviewer

                                                Review Java code carefully.
                                                """);

                runner.run(
                                evaluationCase,
                                skill,
                                engine,
                                workspace,
                                timeout);

                assertThat(capturedRequest.get()).isNotNull();

                assertThat(capturedRequest.get().prompt())
                                .isEqualTo("Analyze this code");

                assertThat(capturedRequest.get().workspace())
                                .isEqualTo(workspace);

                assertThat(capturedRequest.get().timeout())
                                .isEqualTo(timeout);

                assertThat(capturedRequest.get().skill())
                                .isEqualTo(skill);
        }

        @Test
        void shouldEvaluateCaseExpectationsAgainstAgentOutput() {

                AgentEngine engine = request -> new AgentExecutionResult(
                                ExecutionStatus.SUCCESS,
                                "I found null-safety problems in this code.",
                                "",
                                Duration.ofMillis(10));

                var expectationEvaluator = new ExpectationEvaluator();
                var runner = new CaseRunner(expectationEvaluator);

                var evaluationCase = new EvaluationCaseSpec(
                                "case-001",
                                "Null safety",
                                "Review this code",
                                new ExpectationSpec(
                                                List.of(
                                                                "null-safety",
                                                                "problems")));

                var skill = new Skill(
                                workspace.resolve("SKILL.md"),
                                "# Java Reviewer");

                var result = runner.run(
                                evaluationCase,
                                skill,
                                engine,
                                workspace,
                                Duration.ofSeconds(30));

                assertThat(result.expectation())
                                .isNotNull();

                assertThat(result.expectation().passed())
                                .isTrue();

                assertThat(result.expectation().missingOutputContains())
                                .isEmpty();

                assertThat(result.evaluationStatus())
                                .isEqualTo(CaseEvaluationStatus.PASSED);
        }

        @Test
        void shouldMarkCaseAsFailedWhenExpectationIsNotSatisfied() {

                AgentEngine engine = request -> new AgentExecutionResult(
                                ExecutionStatus.SUCCESS,
                                "I found null-safety problems in this code.",
                                "",
                                Duration.ofMillis(10));

                var runner = new CaseRunner(
                                new ExpectationEvaluator());

                var evaluationCase = new EvaluationCaseSpec(
                                "case-001",
                                "Null safety",
                                "Review this code",
                                new ExpectationSpec(
                                                List.of(
                                                                "null-safety",
                                                                "transaction")));

                var skill = new Skill(
                                workspace.resolve("SKILL.md"),
                                "# Java Reviewer");

                var result = runner.run(
                                evaluationCase,
                                skill,
                                engine,
                                workspace,
                                Duration.ofSeconds(30));

                assertThat(result.status())
                                .isEqualTo(ExecutionStatus.SUCCESS);

                assertThat(result.expectation().passed())
                                .isFalse();

                assertThat(result.expectation().missingOutputContains())
                                .containsExactly("transaction");

                assertThat(result.evaluationStatus())
                                .isEqualTo(CaseEvaluationStatus.FAILED);
        }

        @Test
        void shouldMarkCaseAsNotEvaluatedWhenThereAreNoExpectations() {

                AgentEngine engine = request -> new AgentExecutionResult(
                                ExecutionStatus.SUCCESS,
                                "some output",
                                "",
                                Duration.ofMillis(10));

                var runner = new CaseRunner(
                                new ExpectationEvaluator());

                var evaluationCase = new EvaluationCaseSpec(
                                "case-001",
                                "Example",
                                "Do something");

                var skill = new Skill(
                                workspace.resolve("SKILL.md"),
                                "# Test Skill");

                var result = runner.run(
                                evaluationCase,
                                skill,
                                engine,
                                workspace,
                                Duration.ofSeconds(30));

                assertThat(result.expectation())
                                .isNull();

                assertThat(result.evaluationStatus())
                                .isEqualTo(CaseEvaluationStatus.NOT_EVALUATED);
        }

        @Test
        void shouldEvaluateAgentOutputWithSemanticJudge() {

                AgentEngine engine = request -> new AgentExecutionResult(
                                ExecutionStatus.SUCCESS,
                                "The code may throw NullPointerException because the value can be null.",
                                "",
                                Duration.ofMillis(10));

                SemanticJudge semanticJudge = request -> new JudgeResult(
                                true,
                                "The response correctly identifies the null-safety issue.");

                var runner = new CaseRunner(
                                new ExpectationEvaluator(),
                                semanticJudge);

                var evaluationCase = new EvaluationCaseSpec(
                                "case-001",
                                "Null safety",
                                "Review this Java code",
                                null,
                                new JudgeSpec(
                                                "The response must correctly identify the null-safety problem."));

                var skill = new Skill(
                                workspace.resolve("SKILL.md"),
                                "# Java Reviewer");

                var result = runner.run(
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
                                .contains("null-safety");
        }

}