package dev.springeval.engine;

import dev.springeval.evaluation.ProcessEngineSpec;

import java.util.ArrayList;

public class ProcessAgentEngine implements AgentEngine {

    private final ProcessRunner processRunner;
    private final ProcessEngineSpec engineSpec;

    public ProcessAgentEngine(
            ProcessRunner processRunner,
            ProcessEngineSpec engineSpec) {
        this.processRunner = processRunner;
        this.engineSpec = engineSpec;
    }

    @Override
    public AgentExecutionResult execute(
            AgentExecutionRequest request) {

        var command = new ArrayList<String>();

        command.add(engineSpec.command());

        if (engineSpec.args() != null) {
            command.addAll(engineSpec.args());
        }

        var agentInput = """
                %s

                ## Task

                %s
                """.formatted(
                request.skill().content(),
                request.prompt());

        command.add(agentInput);

        var processRequest = new ProcessExecutionRequest(
                command,
                request.workspace(),
                request.timeout());

        var result = processRunner.execute(processRequest);

        return new AgentExecutionResult(
                result.status(),
                result.stdout(),
                result.stderr(),
                result.duration());
    }
}