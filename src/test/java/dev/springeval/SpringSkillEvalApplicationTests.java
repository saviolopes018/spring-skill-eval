package dev.springeval;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringSkillEvalApplicationTests {

	@Test
	void shouldUseInteractiveModeWhenNoArgumentsAreProvided() {
		assertThat(
				SpringSkillEvalApplication.isInteractive(new String[0])).isTrue();
	}

	@Test
	void shouldUseNonInteractiveModeWhenArgumentsAreProvided() {
		assertThat(
				SpringSkillEvalApplication.isInteractive(
						new String[] {
								"run",
								"evaluation.yaml"
						}))
				.isFalse();
	}

	@Test
	void shouldEnableInteractiveShellWhenNoArgumentsAreProvided() {
		var properties = SpringSkillEvalApplication.shellProperties(
				new String[0]);

		assertThat(properties)
				.containsEntry(
						"spring.shell.interactive.enabled",
						true);
	}

	@Test
	void shouldDisableInteractiveShellWhenArgumentsAreProvided() {
		var properties = SpringSkillEvalApplication.shellProperties(
				new String[] {
						"run",
						"evaluation.yaml"
				});

		assertThat(properties)
				.containsEntry(
						"spring.shell.interactive.enabled",
						false);
	}

}
