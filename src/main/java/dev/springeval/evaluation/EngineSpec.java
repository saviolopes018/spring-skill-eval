package dev.springeval.evaluation;

import java.util.List;

public record EngineSpec(String type, String command, List<String> args) {
}
