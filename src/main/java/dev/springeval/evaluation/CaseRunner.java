package dev.springeval.evaluation;

import dev.springeval.engine.AgentEngine;
import dev.springeval.engine.AgentExecutionRequest;
import dev.springeval.skill.Skill;

import java.nio.file.Path;
import java.time.Duration;

public class CaseRunner {

        private final ExpectationEvaluator expectationEvaluator;
        private final SemanticJudge semanticJudge;

        public CaseRunner(
                        ExpectationEvaluator expectationEvaluator) {
                this(
                                expectationEvaluator,
                                null);
        }

        public CaseRunner(
                        ExpectationEvaluator expectationEvaluator,
                        SemanticJudge semanticJudge) {
                this.expectationEvaluator = expectationEvaluator;
                this.semanticJudge = semanticJudge;
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

                var expectationResult = evaluationCase.expect() == null
                                ? null
                                : expectationEvaluator.evaluate(
                                                evaluationCase.expect(),
                                                executionResult.output());

                var judgeResult = evaluationCase.judge() == null
                                ? null
                                : semanticJudge.evaluate(
                                                new JudgeRequest(
                                                                evaluationCase.judge(),
                                                                evaluationCase.prompt(),
                                                                executionResult.output()));

                return new CaseResult(
                                evaluationCase.id(),
                                executionResult.status(),
                                executionResult.output(),
                                executionResult.error(),
                                executionResult.duration(),
                                expectationResult,
                                judgeResult);
        }
}