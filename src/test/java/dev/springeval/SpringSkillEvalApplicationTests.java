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

}
