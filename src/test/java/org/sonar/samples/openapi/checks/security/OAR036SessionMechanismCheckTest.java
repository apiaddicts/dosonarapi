package org.sonar.samples.openapi.checks.security;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.openapi.BaseCheckTest;

import apiquality.sonar.openapi.checks.security.OAR036SessionMechanismsCheck;

public class OAR036SessionMechanismCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "OAR036";
        check = new OAR036SessionMechanismsCheck();
        v2Path = getV2Path("security");
        v3Path = getV3Path("security");
    }

    @Test
    public void verifyInV2() {
        verifyV2("valid");
    }

    @Test
    public void verifyInV2WithForbiddenParams() {
        verifyV2("with-cookie");
    }

    @Test
    public void verifyInV3() {
        verifyV3("valid");
    }

    @Test
    public void verifyInV3WithForbiddenParams() {
        verifyV3("with-cookie");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("OAR036 - SessionMechanismCheck - Session mechanisms are forbidden", RuleType.VULNERABILITY, Severity.BLOCKER, tags("safety"));
    }
}
