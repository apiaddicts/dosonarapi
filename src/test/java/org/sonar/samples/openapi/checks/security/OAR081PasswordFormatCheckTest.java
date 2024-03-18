package org.sonar.samples.openapi.checks.security;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.openapi.BaseCheckTest;

import apiquality.sonar.openapi.checks.security.OAR081PasswordFormatCheck;

public class OAR081PasswordFormatCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "OAR081";
        check = new OAR081PasswordFormatCheck();
        v2Path = getV2Path("security");
        v3Path = getV3Path("security");
    }

    @Test
    public void verifyInV2notvalid() {
        verifyV2("not-valid-password");
    }
    @Test
    public void verifyvalidV2() {
        verifyV2("valid-password");
    }

    @Test
    public void verifyInV3notvalid() {
        verifyV3("not-valid-password");
    }
    @Test
    public void verifyvalidV3() {
        verifyV3("valid-password");
    }
    @Test
    public void verifyInV2Components() {
        verifyV2("valid-with-components");
    }

    @Test
    public void verifyInV3Components() {
        verifyV3("valid-with-components");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("OAR081 - PasswordFormat - Fields of type password should be string with format password", RuleType.VULNERABILITY, Severity.MAJOR, tags("safety"));
    }
}
