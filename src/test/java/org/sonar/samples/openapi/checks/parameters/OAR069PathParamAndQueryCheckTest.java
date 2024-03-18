package org.sonar.samples.openapi.checks.parameters;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.openapi.BaseCheckTest;

import apiquality.sonar.openapi.checks.parameters.OAR069PathParamAndQueryCheck;

public class OAR069PathParamAndQueryCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "OAR069";
        check = new OAR069PathParamAndQueryCheck();
        v2Path = getV2Path("parameters");
        v3Path = getV3Path("parameters");
    }

    @Test
    public void verifyInV2BadRequest() {
        verifyV2("bad-request400");
    }

    @Test
    public void verifyInV2NoBadRequest() {
        verifyV2("no-bad-request400");
    }

    @Test
    public void verifyInV3BadRequest() {
        verifyV3("bad-request400");
    }

    @Test
    public void verifyInV3NoBadRequest() {
        verifyV3("no-bad-request400");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("OAR069 - PathParamAndQuery -  Any param in PATH or QUERY, should have bad request (400) response",
                RuleType.BUG, Severity.MAJOR, tags("parameters"));
    }
}