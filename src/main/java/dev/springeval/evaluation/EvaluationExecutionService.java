package dev.springeval.evaluation;

import dev.springeval.engine.AgentEngineFactory;

import java.nio.file.Path;
import java.time.Duration;

public class EvaluationExecutionService {

    private final EvaluationSuiteLoader suiteLoader;
    private final AgentEngineFactory engineFactory;
    private final EvaluationRunner evaluationRunner;

    public EvaluationExecutionService(
            EvaluationSuiteLoader suiteLoader,
            AgentEngineFactory engineFactory,
            EvaluationRunner evaluationRunner) {
        this.suiteLoader = suiteLoader;
        this.engineFactory = engineFactory;
        this.evaluationRunner = evaluationRunner;
    }

    public EvaluationResult execute(
            Path evaluationPath,
            Path workspace,
            Duration timeout) {

        var suite = suiteLoader.load(evaluationPath);

        var engine = engineFactory.create(
                suite.spec().engine());

        return evaluationRunner.run(
                suite.spec().name(),
                suite.cases(),
                engine,
                workspace,
                timeout);
    }
}