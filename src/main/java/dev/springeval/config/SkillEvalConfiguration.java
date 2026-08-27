package dev.springeval.config;

import dev.springeval.engine.AgentEngineFactory;
import dev.springeval.engine.ProcessRunner;
import dev.springeval.evaluation.CaseRunner;
import dev.springeval.evaluation.EvaluationCaseLoader;
import dev.springeval.evaluation.EvaluationLoader;
import dev.springeval.evaluation.EvaluationRunner;
import dev.springeval.evaluation.EvaluationSuiteLoader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SkillEvalConfiguration {

    @Bean
    EvaluationLoader evaluationLoader() {
        return new EvaluationLoader();
    }

    @Bean
    EvaluationCaseLoader evaluationCaseLoader() {
        return new EvaluationCaseLoader();
    }

    @Bean
    EvaluationSuiteLoader evaluationSuiteLoader(
            EvaluationLoader evaluationLoader,
            EvaluationCaseLoader evaluationCaseLoader) {
        return new EvaluationSuiteLoader(
                evaluationLoader,
                evaluationCaseLoader);
    }

    @Bean
    ProcessRunner processRunner() {
        return new ProcessRunner();
    }

    @Bean
    AgentEngineFactory agentEngineFactory(
            ProcessRunner processRunner) {
        return new AgentEngineFactory(processRunner);
    }

    @Bean
    CaseRunner caseRunner() {
        return new CaseRunner();
    }

    @Bean
    EvaluationRunner evaluationRunner(
            CaseRunner caseRunner) {
        return new EvaluationRunner(caseRunner);
    }
}