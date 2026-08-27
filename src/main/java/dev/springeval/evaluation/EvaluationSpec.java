package dev.springeval.evaluation;

import java.util.List;

public record EvaluationSpec(String schemaVersion, String name, SkillSpec skill, EngineSpec engine, List<String> cases,
        EvaluationDefaults defaults) {

}
