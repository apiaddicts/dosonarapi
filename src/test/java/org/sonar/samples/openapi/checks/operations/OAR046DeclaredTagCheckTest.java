package org.sonar.samples.openapi.checks.operations;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.openapi.BaseCheckTest;

import apiquality.sonar.openapi.checks.operations.OAR046DeclaredTagCheck;

public class OAR046DeclaredTagCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "OAR046";
        check = new OAR046DeclaredTagCheck();
        v2Path = getV2Path("operations");
        v3Path = getV3Path("operations");
    }

    @Test
    public void verifyInV2() {
        verifyV2("declared-tag");
    }

    @Test
    public void verifyInV3() {
        verifyV3("declared-tag");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("OAR046 - DeclaredTag - Each operation SHOULD have a tag.", RuleType.BUG, Severity.BLOCKER, tags("operations"));
    }
}
