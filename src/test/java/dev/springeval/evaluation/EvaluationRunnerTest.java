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
import static org.mockito.Mockito.timeout;

import static org.assertj.core.api.Assertions.assertThat;

class EvaluationRunnerTest {

        @TempDir
        Path workspace;

        @Test
        void shouldExecuteAllEvaluationCases() {

                var capturedRequest = new AtomicReference<AgentExecutionRequest>();

                AgentEngine engine = request -> {

                        capturedRequest.set(request);

                        return new AgentExecutionResult(
                                        ExecutionStatus.SUCCESS,
                                        "completed: " + request.prompt(),
                                        "",
                                        Duration.ofMillis(10));
                };

                var caseRunner = new CaseRunner();
                var runner = new EvaluationRunner(caseRunner);

                var cases = List.of(
                                new EvaluationCaseSpec(
                                                "case-001",
                                                "First case",
                                                "prompt one"),
                                new EvaluationCaseSpec(
                                                "case-002",
                                                "Second case",
                                                "prompt two"));

                var skill = new Skill(
                                workspace.resolve("SKILL.md"),
                                """
                                                # Java Reviewer

                                                Review Java code carefully.
                                                """);

                var timeout = Duration.ofSeconds(30);

                var result = runner.run(
                                "example-evaluation",
                                cases,
                                skill,
                                engine,
                                workspace,
                                timeout);

                assertThat(result.evaluationName())
                                .isEqualTo("example-evaluation");

                assertThat(result.cases())
                                .hasSize(2);

                assertThat(result.cases())
                                .extracting(CaseResult::caseId)
                                .containsExactly(
                                                "case-001",
                                                "case-002");

                assertThat(result.cases())
                                .extracting(CaseResult::output)
                                .containsExactly(
                                                "completed: prompt one",
                                                "completed: prompt two");

                assertThat(capturedRequest.get().skill())
                                .isEqualTo(skill);
        }
}