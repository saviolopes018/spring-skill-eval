package dev.springeval.evaluation;

import java.util.List;

import dev.springeval.skill.Skill;

public record EvaluationSuite(

                EvaluationSpec spec,
                Skill skill,
                List<EvaluationCaseSpec> cases

) {

}
