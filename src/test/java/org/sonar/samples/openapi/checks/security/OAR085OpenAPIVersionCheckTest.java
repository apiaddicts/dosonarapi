package org.sonar.samples.openapi.checks.security;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.api.server.rule.RuleParamType;
import org.sonar.samples.openapi.BaseCheckTest;

import apiquality.sonar.openapi.checks.security.OAR085OpenAPIVersionCheck;

public class OAR085OpenAPIVersionCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "OAR085";
        check = new OAR085OpenAPIVersionCheck();
        v2Path = getV2Path("security");
        v3Path = getV3Path("security");
    }

    @Test
    public void verifyInV2() {
        verifyV2("valid-openapi-version");
    }

    @Test
    public void verifyInvalidOpenApiVersionInV2() {
        verifyV2("invalid-openapi-version");
    }

    @Test
    public void verifyInV3() {
        verifyV3("valid-openapi-version");
    }

    @Test
    public void verifyInvalidOpenApiVersionInV3() {
        verifyV3("invalid-openapi-version");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("OAR085 - OpenAPIVersion - The OpenAPI version should be one of the allowed by the organization", RuleType.VULNERABILITY, Severity.MINOR, tags("safety"));
    }

    @Override
    public void verifyParameters() {
        assertNumberOfParameters(1);
        assertParameterProperties("valid-versions", "2.0,3.0.0,3.0.1,3.0.2,3.0.3,3.1", RuleParamType.STRING);
    }
}