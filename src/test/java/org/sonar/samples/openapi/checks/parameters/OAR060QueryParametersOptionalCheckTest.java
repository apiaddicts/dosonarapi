package org.sonar.samples.openapi.checks.parameters;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.openapi.BaseCheckTest;

import apiquality.sonar.openapi.checks.parameters.OAR060QueryParametersOptionalCheck;

public class OAR060QueryParametersOptionalCheckTest extends BaseCheckTest{
    
    @Before
    public void init() {
        ruleName = "OAR060";
        check = new OAR060QueryParametersOptionalCheck();
        v2Path = getV2Path("parameters");
        v3Path = getV3Path("parameters");
    }

    @Test
    public void verifyInV2requiredFalse() {
        verifyV2("required-false");
    }
    @Test
    public void verifyInV2requiredTrue() {
        verifyV2("required-true");
    }
    @Test
    public void verifyInV3requiredFalse() {
        verifyV3("required-false");
    }
    @Test
    public void verifyInV3requiredTrue() {
        verifyV3("required-true");
    }
    @Override
    public void verifyRule() {
        assertRuleProperties("OAR060 - QueryParametersOptional - All parameters in query must be defined as optional", RuleType.BUG, Severity.CRITICAL, tags("parameters"));
    }
    
}
