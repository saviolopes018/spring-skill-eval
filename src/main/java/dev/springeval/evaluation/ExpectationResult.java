package dev.springeval.evaluation;

import java.util.List;

public record ExpectationResult(
        boolean passed,
        List<String> missingOutputContains) {
}