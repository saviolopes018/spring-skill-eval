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

}
