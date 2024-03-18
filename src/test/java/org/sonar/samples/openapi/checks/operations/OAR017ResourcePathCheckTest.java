package org.sonar.samples.openapi.checks.operations;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.openapi.BaseCheckTest;

import apiquality.sonar.openapi.checks.operations.OAR017ResourcePathCheck;

public class OAR017ResourcePathCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "OAR017";
        check = new OAR017ResourcePathCheck();
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
        assertRuleProperties("OAR017 - ResourcePath - Resource path should alternate static and parametrized parts", RuleType.BUG, Severity.MAJOR, tags("operations"));
    }

}