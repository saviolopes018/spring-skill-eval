package dev.springeval.evaluation;

import java.util.List;

import jakarta.validation.constraints.NotBlank;

public record EngineSpec(

        @NotBlank String type,

        @NotBlank String command,

        List<String> args

) {
}
