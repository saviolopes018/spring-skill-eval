# Spring Skill Eval

A Java and Spring-based evaluation toolkit for testing AI agent skills.

Spring Skill Eval executes an agent with a `SKILL.md`, runs predefined evaluation cases, and determines whether the agent behaved as expected.

It supports deterministic expectations and semantic evaluation using Spring AI.

## Why

Writing a skill or system prompt is easy.

Knowing whether it consistently produces the expected behavior is harder.

Spring Skill Eval makes that behavior testable.

```text
SKILL.md
   +
Evaluation cases
   ↓
Agent
   ↓
Output
   ↓
Expect / Judge
   ↓
PASSED / FAILED
```

## Requirements

- Java 25+
- Maven
- An agent CLI available in the system PATH for process-based evaluations

For example:

- Codex CLI
- another CLI-compatible agent

Semantic judges additionally require a Spring AI model provider.

## Build

```bash
./mvnw clean package
```

The executable JAR will be generated under:

```text
target/spring-skill-eval-0.0.1-SNAPSHOT.jar
```

## Quick start

Run the deterministic example:

```bash
java -jar target/spring-skill-eval-0.0.1-SNAPSHOT.jar \
  run examples/process-smoke/eval.yaml
```

Expected result:

```text
Evaluation: process-smoke-eval

basic-001 - PASSED

Passed: 1
Failed: 0
Not evaluated: 0
Score: 100.0%
Status: PASSED
```

## Evaluation file

An evaluation is defined through an `eval.yaml` file:

```yaml
schema_version: v1alpha1

name: java-reviewer

skill:
  path: ./skill

engine:
  type: process
  command: codex
  args:
    - exec
    - "--"

cases:
  - ./cases/null-safety.yaml

defaults:
  timeout: 120s
```

Paths are resolved relative to the evaluation file.

## Skill

The skill directory must contain a `SKILL.md` file.

Example:

```markdown
---
name: java-reviewer
description: Reviews Java code.
---

# Java Reviewer

You are a senior Java code reviewer.

Focus on correctness, null-safety and maintainability.
```

The skill content is combined with the evaluation case prompt before being sent to the configured agent.

## Evaluation cases

Each case is stored in its own YAML file.

```yaml
id: null-safety-001
name: Detect possible null dereference

prompt: |
  Review this Java code and identify the main problem.
```

A case can be evaluated using `expect`, `judge`, or both.

## Deterministic expectations

Use `expect` when the output must contain predictable values:

```yaml
expect:
  output_contains:
    - "null-safety"
    - "NullPointerException"
```

This evaluation does not require another LLM.

## Semantic judge

Use `judge` when correctness depends on meaning rather than exact wording:

```yaml
judge:
  criteria: |
    The response must identify that user or user.getName()
    may be null and cause a NullPointerException.
```

The agent response is evaluated by a Spring AI model.

### OpenAI judge

Enable the OpenAI provider:

```bash
export SPRING_AI_MODEL_CHAT=openai
export OPENAI_API_KEY="..."
export OPENAI_MODEL=gpt-5-mini
```

Then run:

```bash
java -jar target/spring-skill-eval-0.0.1-SNAPSHOT.jar \
  run examples/codex-java-reviewer-judge/eval.yaml
```

The agent and the judge are independent:

```text
SKILL.md + Case
      ↓
Codex
      ↓
Agent response
      ↓
Spring AI
      ↓
Semantic Judge
      ↓
PASSED / FAILED
```

## Evaluation status

A case can result in:

- `PASSED` — execution and configured evaluations succeeded
- `FAILED` — execution, deterministic expectation, or semantic judge failed
- `NOT_EVALUATED` — the case has neither `expect` nor `judge`

An evaluation can result in:

- `PASSED`
- `FAILED`
- `INCOMPLETE`

`NOT_EVALUATED` cases are excluded from the score denominator.

## Process engine

The first supported engine is `process`.

```yaml
engine:
  type: process
  command: codex
  args:
    - exec
    - "--"
```

The configured process receives the combined skill instructions and case prompt as its final CLI argument.

The process engine captures:

- stdout
- stderr
- exit code
- execution duration
- timeout

## Examples

### Process smoke test

```text
examples/process-smoke
```

Uses `printf` and requires no AI provider.

### Codex Java reviewer

```text
examples/codex-java-reviewer
```

Executes a real Codex agent and evaluates its output deterministically.

### Codex + semantic judge

```text
examples/codex-java-reviewer-judge
```

Executes Codex as the agent and Spring AI as the semantic judge.

## Current scope

The current version focuses on:

- YAML evaluation definitions
- `SKILL.md` loading
- process-based agent execution
- deterministic expectations
- semantic judges through Spring AI
- timeout handling
- Spring Shell CLI
- interactive and non-interactive execution

Future versions may include additional agent adapters, richer reports, parallel execution, retries, observability, and isolated execution environments.
