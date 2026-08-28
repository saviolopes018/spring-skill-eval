package dev.springeval.evaluation;

@FunctionalInterface
public interface SemanticJudge {

    JudgeResult evaluate(JudgeRequest request);
}