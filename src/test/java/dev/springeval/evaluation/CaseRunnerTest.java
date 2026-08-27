package dev.springeval.evaluation;

import dev.springeval.engine.AgentEngine;
import dev.springeval.engine.AgentExecutionRequest;
import dev.springeval.engine.AgentExecutionResult;
import dev.springeval.engine.ExecutionStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.time.Duration;

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

        var runner = new CaseRunner(engine);

        var evaluationCase = new EvaluationCaseSpec(
                "null-safety-001",
                "Detect null safety issue",
                "Review this Java code");

        var result = runner.run(
                evaluationCase,
                workspace,
                Duration.ofSeconds(30));

        assertThat(result.caseId())
                .isEqualTo("null-safety-001");

        assertThat(result.status())
                .isEqualTo(ExecutionStatus.SUCCESS);

        assertThat(result.output())
                .isEqualTo("review completed");
    }
}