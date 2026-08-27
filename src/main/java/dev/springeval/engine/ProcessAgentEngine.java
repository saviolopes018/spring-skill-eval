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
    public AgentExecutionResult execute(AgentExecutionRequest request) {

        var command = new ArrayList<String>();

        command.add(engineSpec.command());
        command.addAll(engineSpec.args());
        command.add(request.prompt());

        var processRequest = new ProcessExecutionRequest(
                command,
                request.workspace(),
                request.timeout());

        var processResult = processRunner.execute(processRequest);

        return new AgentExecutionResult(
                processResult.status(),
                processResult.stdout(),
                processResult.stderr(),
                processResult.duration());
    }
}