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
            Path workspace) {

        var suite = suiteLoader.load(evaluationPath);

        var engine = engineFactory.create(
                suite.spec().engine());

        var timeout = parseTimeout(
                suite.spec().defaults().timeout());

        return evaluationRunner.run(
                suite.spec().name(),
                suite.cases(),
                suite.skill(),
                engine,
                workspace,
                timeout);
    }

    private Duration parseTimeout(String value) {

        if (value.endsWith("ms")) {
            return Duration.ofMillis(
                    Long.parseLong(
                            value.substring(0, value.length() - 2)));
        }

        if (value.endsWith("s")) {
            return Duration.ofSeconds(
                    Long.parseLong(
                            value.substring(0, value.length() - 1)));
        }

        if (value.endsWith("m")) {
            return Duration.ofMinutes(
                    Long.parseLong(
                            value.substring(0, value.length() - 1)));
        }

        throw new IllegalArgumentException(
                "Unsupported timeout format: " + value);
    }
}