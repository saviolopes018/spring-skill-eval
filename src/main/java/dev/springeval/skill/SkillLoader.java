package dev.springeval.skill;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class SkillLoader {

    public Skill load(Path directory) {

        var skillPath = directory.resolve("SKILL.md");

        try {
            var content = Files.readString(
                    skillPath,
                    StandardCharsets.UTF_8);

            return new Skill(
                    skillPath,
                    content);

        } catch (IOException exception) {
            throw new UncheckedIOException(
                    "Failed to load skill from " + skillPath,
                    exception);
        }
    }
}