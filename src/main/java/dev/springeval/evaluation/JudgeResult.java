package dev.springeval.evaluation;

public record JudgeResult(
        boolean passed,
        String reasoning

) {
}