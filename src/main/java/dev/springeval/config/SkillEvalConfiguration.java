package dev.springeval.config;

import dev.springeval.engine.AgentEngineFactory;
import dev.springeval.engine.ProcessRunner;
import dev.springeval.evaluation.CaseRunner;
import dev.springeval.evaluation.EvaluationCaseLoader;
import dev.springeval.evaluation.EvaluationExecutionService;
import dev.springeval.evaluation.EvaluationLoader;
import dev.springeval.evaluation.EvaluationRunner;
import dev.springeval.evaluation.EvaluationSuiteLoader;
import dev.springeval.evaluation.ExpectationEvaluator;
import dev.springeval.evaluation.JudgePromptBuilder;
import dev.springeval.skill.SkillLoader;
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
            EvaluationCaseLoader evaluationCaseLoader,
            SkillLoader skillLoader) {
        return new EvaluationSuiteLoader(
                evaluationLoader,
                evaluationCaseLoader,
                skillLoader);
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
    CaseRunner caseRunner(
            ExpectationEvaluator expectationEvaluator) {
        return new CaseRunner(
                expectationEvaluator);
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

    @Bean
    SkillLoader skillLoader() {
        return new SkillLoader();
    }

    @Bean
    ExpectationEvaluator expectationEvaluator() {
        return new ExpectationEvaluator();
    }

    @Bean
    JudgePromptBuilder judgePromptBuilder() {
        return new JudgePromptBuilder();
    }
}