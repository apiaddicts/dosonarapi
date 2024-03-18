package org.sonar.samples.openapi.checks.security;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.openapi.BaseCheckTest;

import apiquality.sonar.openapi.checks.security.OAR045DefinedResponseCheck;

public class OAR045DefinedResponseCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "OAR045";
        check = new OAR045DefinedResponseCheck();
        v2Path = getV2Path("security");
        v3Path = getV3Path("security");
    }

    @Test
    public void verifyInV2() {
        verifyV2("defined-response");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("OAR045 - DefinedResponse - Each operation MUST have at least one defined response with a defined model.", RuleType.BUG, Severity.BLOCKER, tags("safety"));
    }
}
