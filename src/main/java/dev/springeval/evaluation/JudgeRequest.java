package dev.springeval.evaluation;

public record JudgeRequest(
        JudgeSpec spec,
        String prompt,
        String output

) {
}