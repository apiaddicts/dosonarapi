package org.sonar.samples.openapi.checks.operations;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.api.server.rule.RuleParamType;
import org.sonar.samples.openapi.BaseCheckTest;

import apiquality.sonar.openapi.checks.operations.OAR014ResourceLevelWithinNonSuggestedRangeCheck;

public class OAR014ResourceLevelWithinNonSuggestedRangeCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "OAR014";
        check = new OAR014ResourceLevelWithinNonSuggestedRangeCheck();
        v2Path = getV2Path("operations");
        v3Path = getV3Path("operations");
    }

    @Test
    public void verifyInV2() {
        verifyV2("plain");
    }

    @Test
    public void verifyInV3() {
        verifyV3("plain");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("OAR014 - ResourceLevelWithinNonSuggestedRangeCheck - Resources depth level should be below the non-suggested range", RuleType.BUG, Severity.MINOR, tags("operations"));
    }

    @Override
    public void verifyParameters() {
        assertNumberOfParameters(2);
        assertParameterProperties("min-level", "4", RuleParamType.INTEGER);
        assertParameterProperties("max-level", "5", RuleParamType.INTEGER);
    }
}
