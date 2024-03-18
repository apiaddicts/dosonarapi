package org.sonar.samples.openapi.checks.security;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.api.server.rule.RuleParamType;
import org.sonar.samples.openapi.BaseCheckTest;

import apiquality.sonar.openapi.checks.security.OAR035UnauthorizedResponseCheck;

public class OAR035AuthorizationResponsesCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "OAR035";
        check = new OAR035UnauthorizedResponseCheck();
        v2Path = getV2Path("security");
        v3Path = getV3Path("security");
    }

    @Test
    public void verifyInV2() {
        verifyV2("valid");
    }

    @Test
    public void verifyInV2WithoutAuthorizationResponses() {
        verifyV2("without-authorization-responses");
    }

    @Test
    public void verifyInV3() {
        verifyV3("valid");
    }

    @Test
    public void verifyInV3WithoutAuthorizationResponses() {
        verifyV3("without-authorization-responses");
    }

    @Override
    public void verifyParameters() {
        assertNumberOfParameters(1);
        assertParameterProperties("expected-codes", "401", RuleParamType.STRING);
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("OAR035 - AuthorizationResponses - When defining security schemes, authorization response codes must be defined", RuleType.VULNERABILITY, Severity.MAJOR, tags("safety"));
    }
}