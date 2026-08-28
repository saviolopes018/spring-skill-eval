package dev.springeval;

import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringSkillEvalApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSkillEvalApplication.class, args);
	}

	static boolean isInteractive(String[] args) {
		return args.length == 0;
	}

	static Map<String, Object> shellProperties(String[] args) {
		return Map.of(
				"spring.shell.interactive.enabled",
				isInteractive(args));
	}

}
