package dev.springeval.cli;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.shell.core.command.CommandRegistry;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class EvaluationCommandSpringWiringTest {

    @Autowired
    CommandRegistry commandRegistry;

    @Test
    void shouldRegisterRunCommand() {

        assertThat(
                commandRegistry.getCommandByName("run")).isNotNull();
    }
}