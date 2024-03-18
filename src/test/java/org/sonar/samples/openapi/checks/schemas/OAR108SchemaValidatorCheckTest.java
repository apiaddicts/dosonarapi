package org.sonar.samples.openapi.checks.schemas;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.openapi.BaseCheckTest;

import apiquality.sonar.openapi.checks.schemas.OAR108SchemaValidatorCheck;

public class OAR108SchemaValidatorCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "OAR108";
        check = new OAR108SchemaValidatorCheck();
        v2Path = getV2Path("schemas");
        v3Path = getV3Path("schemas");
    }

    @Test
    public void verifyInV2() {
        verifyV2("valid");
    }

    @Test
    public void verifyInV2Invalid() {
        verifyV2("invalid");
    }

    @Test
    public void verifyInV3() {
        verifyV3("valid");
    }

    @Test
    public void verifyInV3Invalid() {
        verifyV3("invalid");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("OAR108 - SchemaValidator - Schema does not match the provided example", RuleType.BUG, Severity.MAJOR, tags("schemas"));
    }
}

