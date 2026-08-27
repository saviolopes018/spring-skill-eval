package dev.springeval.engine;

import java.nio.file.Path;
import java.time.Duration;

import dev.springeval.skill.Skill;

public record AgentExecutionRequest(
                Skill skill,
                String prompt,
                Path workspace,
                Duration timeout

) {

        public AgentExecutionRequest(
                        String prompt,
                        Path workspace,
                        Duration timeout) {
                this(
                                null,
                                prompt,
                                workspace,
                                timeout);
        }

}
