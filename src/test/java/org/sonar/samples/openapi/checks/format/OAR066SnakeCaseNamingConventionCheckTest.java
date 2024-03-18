package org.sonar.samples.openapi.checks.format;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.openapi.BaseCheckTest;

import apiquality.sonar.openapi.checks.format.OAR066SnakeCaseNamingConventionCheck;

public class OAR066SnakeCaseNamingConventionCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "OAR066";
        check = new OAR066SnakeCaseNamingConventionCheck();
        v2Path = getV2Path("format");
        v3Path = getV3Path("format");
    }

    @Test
    public void verifyInV2() {
        verifyV2("snake-case-error");
    }
    @Test
    public void verifyvalidV2() {
        verifyV3("valid");
    }

    @Test
    public void verifyInV3() {
        verifyV3("snake-case-error");
    }
    @Test
    public void verifyvalidV3() {
        verifyV3("valid");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("OAR066 - SnakeCaseNamingConvention - RequestBody and Responses schema property names must be compliant with the snake_case naming convention", RuleType.BUG, Severity.MINOR, tags("format"));
    }
}