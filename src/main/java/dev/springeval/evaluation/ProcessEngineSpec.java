package dev.springeval.evaluation;

import java.util.List;

import jakarta.validation.constraints.NotBlank;

public record ProcessEngineSpec(

        @NotBlank String command,

        List<String> args

) implements EngineSpec {

}
