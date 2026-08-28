package dev.springeval.evaluation;

public class JudgePromptBuilder {

    public String build(JudgeRequest request) {

        return """
                You are evaluating the response produced by an AI agent.

                Evaluation criteria:
                %s

                Original task:
                %s

                Agent response:
                %s

                Determine whether the response satisfies the evaluation criteria.
                """.formatted(
                request.spec().criteria(),
                request.prompt(),
                request.output());
    }
}