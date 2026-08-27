package dev.springeval.engine;

import java.nio.file.Path;
import java.time.Duration;
import java.util.List;

public record ExecutionRequest(

                List<String> command,
                Path workspace,
                Duration timeout

) {

}
