package dev.springeval.evaluation;

import dev.springeval.engine.AgentEngine;
import dev.springeval.engine.AgentExecutionRequest;
import dev.springeval.skill.Skill;

import java.nio.file.Path;
import java.time.Duration;

public class CaseRunner {

        public CaseResult run(
                        EvaluationCaseSpec evaluationCase,
                        AgentEngine agentEngine,
                        Path workspace,
                        Duration timeout) {
                return run(
                                evaluationCase,
                                null,
                                agentEngine,
                                workspace,
                                timeout);
        }

        public CaseResult run(
                        EvaluationCaseSpec evaluationCase,
                        Skill skill,
                        AgentEngine agentEngine,
                        Path workspace,
                        Duration timeout) {

                var request = new AgentExecutionRequest(
                                skill,
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