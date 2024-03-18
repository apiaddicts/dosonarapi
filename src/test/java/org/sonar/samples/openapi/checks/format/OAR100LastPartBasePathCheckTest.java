package org.sonar.samples.openapi.checks.format;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.openapi.BaseCheckTest;
import apiquality.sonar.openapi.checks.format.OAR100LastPartBasePathCheck;

public class OAR100LastPartBasePathCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "OAR100";
        check = new OAR100LastPartBasePathCheck();
        v2Path = getV2Path("format");
        v3Path = getV3Path("format");
    }

    @Test
    public void verifyInV2() {
        verifyV2("valid");
    }

    @Test
    public void verifyInV2IncorrectVersion() {
        verifyV2("incorrect-version");
    }

    @Test
    public void verifyInV3() {
        verifyV3("valid");
    }

    @Test
    public void verifyInV3IncorrectVersion() {
        verifyV3("incorrect-version");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("OAR100 - LastPartBasePath - The last part of the path should be the API version", RuleType.BUG, Severity.CRITICAL, tags("format"));
    }

    @Override
    public void verifyParameters() {
        assertNumberOfParameters(0);
    }
}