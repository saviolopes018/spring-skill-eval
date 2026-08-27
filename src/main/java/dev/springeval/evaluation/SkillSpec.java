package dev.springeval.evaluation;

import jakarta.validation.constraints.NotBlank;

public record SkillSpec(

        @NotBlank String path

) {
}
