package dev.springeval.evaluation;

import jakarta.validation.constraints.NotBlank;

public record EvaluationDefaults(

        @NotBlank String timeout

) {
}
