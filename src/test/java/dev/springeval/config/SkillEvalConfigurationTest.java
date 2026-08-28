package dev.springeval.config;

import dev.springeval.engine.AgentEngineFactory;
import dev.springeval.engine.ProcessRunner;
import dev.springeval.evaluation.CaseRunner;
import dev.springeval.evaluation.EvaluationCaseLoader;
import dev.springeval.evaluation.EvaluationLoader;
import dev.springeval.evaluation.EvaluationRunner;
import dev.springeval.evaluation.EvaluationSuiteLoader;
import dev.springeval.evaluation.JudgePromptBuilder;
import jakarta.validation.Validator;
import tools.jackson.dataformat.yaml.YAMLMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SkillEvalConfigurationTest {

    @Autowired
    EvaluationLoader evaluationLoader;

    @Autowired
    EvaluationCaseLoader evaluationCaseLoader;

    @Autowired
    EvaluationSuiteLoader evaluationSuiteLoader;

    @Autowired
    ProcessRunner processRunner;

    @Autowired
    AgentEngineFactory agentEngineFactory;

    @Autowired
    CaseRunner caseRunner;

    @Autowired
    EvaluationRunner evaluationRunner;

    @Autowired
    YAMLMapper yamlMapper;

    @Autowired
    Validator validator;

    @Autowired
    JudgePromptBuilder judgePromptBuilder;

    @Test
    void shouldConfigureCoreEvaluationComponents() {

        assertThat(evaluationLoader).isNotNull();
        assertThat(evaluationCaseLoader).isNotNull();
        assertThat(evaluationSuiteLoader).isNotNull();
        assertThat(processRunner).isNotNull();
        assertThat(agentEngineFactory).isNotNull();
        assertThat(caseRunner).isNotNull();
        assertThat(evaluationRunner).isNotNull();
        assertThat(yamlMapper).isNotNull();
        assertThat(validator).isNotNull();
        assertThat(judgePromptBuilder).isNotNull();
    }
}