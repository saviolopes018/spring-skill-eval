package dev.springeval.evaluation;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record EvaluationSpec(

        @NotBlank String schemaVersion,

        @NotBlank String name,

        @NotNull @Valid SkillSpec skill,

        @NotNull @Valid EngineSpec engine,

        @NotEmpty List<String> cases,

        @NotNull @Valid EvaluationDefaults defaults

) {

}
