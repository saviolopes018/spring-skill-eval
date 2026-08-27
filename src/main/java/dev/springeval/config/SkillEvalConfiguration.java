package dev.springeval.config;

import dev.springeval.engine.AgentEngineFactory;
import dev.springeval.engine.ProcessRunner;
import dev.springeval.evaluation.CaseRunner;
import dev.springeval.evaluation.EvaluationCaseLoader;
import dev.springeval.evaluation.EvaluationExecutionService;
import dev.springeval.evaluation.EvaluationLoader;
import dev.springeval.evaluation.EvaluationRunner;
import dev.springeval.evaluation.EvaluationSuiteLoader;
import jakarta.validation.Validator;
import tools.jackson.databind.PropertyNamingStrategies;
import tools.jackson.dataformat.yaml.YAMLMapper;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SkillEvalConfiguration {

    @Bean
    EvaluationLoader evaluationLoader(
            YAMLMapper yamlMapper,
            Validator validator) {
        return new EvaluationLoader(
                yamlMapper,
                validator);
    }

    @Bean
    EvaluationCaseLoader evaluationCaseLoader(
            YAMLMapper yamlMapper) {
        return new EvaluationCaseLoader(
                yamlMapper);
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

    @Bean
    YAMLMapper yamlMapper() {
        return YAMLMapper.builder()
                .propertyNamingStrategy(
                        PropertyNamingStrategies.SNAKE_CASE)
                .build();
    }

    @Bean
    EvaluationExecutionService evaluationExecutionService(
            EvaluationSuiteLoader suiteLoader,
            AgentEngineFactory engineFactory,
            EvaluationRunner evaluationRunner) {
        return new EvaluationExecutionService(
                suiteLoader,
                engineFactory,
                evaluationRunner);
    }
}