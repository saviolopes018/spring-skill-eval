package dev.springeval.engine;

import dev.springeval.evaluation.ProcessEngineSpec;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AgentEngineFactoryTest {

    @Test
    void shouldCreateProcessAgentEngineFromProcessEngineSpec() {

        var processRunner = new ProcessRunner();
        var factory = new AgentEngineFactory(processRunner);

        var spec = new ProcessEngineSpec(
                "printf",
                List.of("%s"));

        var engine = factory.create(spec);

        assertThat(engine)
                .isInstanceOf(ProcessAgentEngine.class);
    }
}