package org.sonar.samples.openapi.checks.operations;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.openapi.BaseCheckTest;
import apiquality.sonar.openapi.checks.operations.OAR109ForbiddenInternalServerErrorCheck;

public class OAR109ForbiddenInternalServerErrorCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "OAR109";
        check = new OAR109ForbiddenInternalServerErrorCheck();
        v2Path = getV2Path("operations");
        v3Path = getV3Path("operations");
    }

    @Test
    public void verifyInV2() {
        verifyV2("valid");
    }

    @Test
    public void verifyInV3() {
        verifyV3("plain");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("OAR109 - ForbiddenInternalServerErrorCheckTest - Use default instead of directly specifying 5XX codes", RuleType.BUG, Severity.MAJOR, tags("operations"));
    }

}