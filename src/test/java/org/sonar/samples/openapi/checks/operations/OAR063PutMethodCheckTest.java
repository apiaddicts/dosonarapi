package org.sonar.samples.openapi.checks.operations;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.api.server.rule.RuleParamType;
import org.sonar.samples.openapi.BaseCheckTest;

import apiquality.sonar.openapi.checks.operations.OAR063PutMethodCheck;

public class OAR063PutMethodCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "OAR063";
        check = new OAR063PutMethodCheck();
        v2Path = getV2Path("operations");
        v3Path = getV3Path("operations");
    }

    @Test
    public void verifyInV2() {
        verifyV2("valid");
    }

    @Test
    public void verifyInV2InsufficentResponseCodes() {
        verifyV2("insuficent-response-codes");
    }

    @Test
    public void verifyInV3() {
        verifyV3("valid");
    }

    @Test
    public void verifyInV3InsufficentResponseCodes() {
        verifyV3("insuficent-response-codes");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("OAR063 - PutMethod - Response codes in PUT operations must be defined according to the standard", RuleType.BUG, Severity.MAJOR, tags("operations"));
    }

    @Override
    public void verifyParameters() {
        assertNumberOfParameters(3);
        assertParameterProperties("mandatory-response-codes", "200, 202, 204, 206", RuleParamType.STRING);
        assertParameterProperties("paths", "/status, /another", RuleParamType.STRING);
        assertParameterProperties("pathValidationStrategy", "/exclude", RuleParamType.STRING);
    }
}