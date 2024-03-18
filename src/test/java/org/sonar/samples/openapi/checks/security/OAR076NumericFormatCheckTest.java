package org.sonar.samples.openapi.checks.security;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.openapi.BaseCheckTest;

import apiquality.sonar.openapi.checks.security.OAR076NumericFormatCheck;

public class OAR076NumericFormatCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "OAR076";
        check = new OAR076NumericFormatCheck();
        v2Path = getV2Path("security");
        v3Path = getV3Path("security");
    }

    @Test
    public void verifyInV2() {
        verifyV2("plain");
    }

    @Test
    public void verifyInV2WithNested() {
        verifyV2("nested");
    }

    @Test
    public void verifyInV2With$ref() {
        verifyV2("with-$ref");
    }

    @Test
    public void verifyInV3() {
        verifyV3("plain");
    }

    @Test
    public void verifyInV3WithNested() {
        verifyV3("nested");
    }

    @Test
    public void verifyInV3With$ref() {
        verifyV3("with-$ref");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("OAR076 - NumericFormat - Numeric types requires a valid format", RuleType.VULNERABILITY, Severity.MINOR, tags("safety"));
    }
}