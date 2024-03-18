package org.sonar.samples.openapi.checks.operations;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.openapi.BaseCheckTest;

import apiquality.sonar.openapi.checks.operations.OAR047DocumentedTagCheck;

public class OAR047DocumentedTagCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "OAR047";
        check = new OAR047DocumentedTagCheck();
        v2Path = getV2Path("operations");
        v3Path = getV3Path("operations");
    }

    @Test
    public void verifyInV2() {
        verifyV2("documented-tag");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("OAR047 - DocumentedTag - Tags SHOULD be documented.", RuleType.BUG, Severity.BLOCKER, tags("operations"));
    }
}
