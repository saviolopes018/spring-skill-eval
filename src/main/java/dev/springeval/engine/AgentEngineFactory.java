package dev.springeval.engine;

import dev.springeval.evaluation.EngineSpec;
import dev.springeval.evaluation.ProcessEngineSpec;

public class AgentEngineFactory {

    private final ProcessRunner processRunner;

    public AgentEngineFactory(ProcessRunner processRunner) {
        this.processRunner = processRunner;
    }

    public AgentEngine create(EngineSpec spec) {
        return switch (spec) {
            case ProcessEngineSpec processSpec ->
                new ProcessAgentEngine(
                        processRunner,
                        processSpec);
        };
    }
}