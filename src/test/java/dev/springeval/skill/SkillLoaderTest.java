package dev.springeval.skill;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class SkillLoaderTest {

    @Test
    void shouldLoadSkillFromDirectory() {

        var loader = new SkillLoader();

        var resource = getClass()
                .getClassLoader()
                .getResource("skills/java-reviewer");

        assertThat(resource).isNotNull();

        var skill = loader.load(
                Path.of(resource.getPath()));

        assertThat(skill.path())
                .endsWith(
                        Path.of(
                                "skills",
                                "java-reviewer",
                                "SKILL.md"));

        assertThat(skill.content())
                .contains("# Java Reviewer");

        assertThat(skill.content())
                .contains("null-safety");
    }
}