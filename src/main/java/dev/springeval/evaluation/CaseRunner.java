package dev.springeval.evaluation;

import dev.springeval.engine.AgentEngine;
import dev.springeval.engine.AgentExecutionRequest;

import java.nio.file.Path;
import java.time.Duration;

public class CaseRunner {

    private final AgentEngine agentEngine;

    public CaseRunner(AgentEngine agentEngine) {
        this.agentEngine = agentEngine;
    }

    public CaseResult run(
            EvaluationCaseSpec evaluationCase,
            Path workspace,
            Duration timeout) {

        var request = new AgentExecutionRequest(
                evaluationCase.prompt(),
                workspace,
                timeout);

        var executionResult = agentEngine.execute(request);

        return new CaseResult(
                evaluationCase.id(),
                executionResult.status(),
                executionResult.output(),
                executionResult.error(),
                executionResult.duration());
    }
}