package dev.springeval.engine;

import java.nio.file.Path;
import java.time.Duration;

public record AgentExecutionRequest(

        String prompt,
        Path workspace,
        Duration timeout

) {

}
