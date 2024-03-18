package org.sonar.samples.openapi.checks.security;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.openapi.BaseCheckTest;

import apiquality.sonar.openapi.checks.security.OAR049NoContentIn204Check;

public class OAR049NoContentIn204CheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "OAR049";
        check = new OAR049NoContentIn204Check();
        v2Path = getV2Path("security");
        v3Path = getV3Path("security");
    }

    @Test
    public void verifyInV2() {
        verifyV2("no-content-in-204");
    }

    @Test
    public void verifyInV3() {
        verifyV3("no-content-in-204");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("OAR049 - NoContent204 - 204 responses MUST NOT return content.", RuleType.BUG, Severity.BLOCKER, tags("safety"));
    }
}
