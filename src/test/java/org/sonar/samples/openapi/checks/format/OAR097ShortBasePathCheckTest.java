package org.sonar.samples.openapi.checks.format;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.openapi.BaseCheckTest;
import apiquality.sonar.openapi.checks.format.OAR097ShortBasePathCheck;

public class OAR097ShortBasePathCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "OAR097";
        check = new OAR097ShortBasePathCheck();
        v2Path = getV2Path("format");
        v3Path = getV3Path("format");
    }

    @Test
    public void verifyInV2() {
        verifyV2("valid");
    }

    @Test
    public void verifyInV2TooShort() {
        verifyV2("too-short");
    }

    @Test
    public void verifyInV3() {
        verifyV3("valid");
    }

    @Test
    public void verifyInV3TooShort() {
        verifyV3("too-short");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("OAR097 - ShortBasePath - Base path must have at least two parts", RuleType.BUG, Severity.CRITICAL, tags("format"));
    }

    @Override
    public void verifyParameters() {
        assertNumberOfParameters(0);
    }
}