package org.sonar.samples.openapi.checks.security;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.api.server.rule.RuleParamType;
import org.sonar.samples.openapi.BaseCheckTest;

import apiquality.sonar.openapi.checks.security.OAR084ForbiddenFormatsInQueryCheck;

public class OAR084ForbiddenFormatsInQueryCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "OAR084";
        check = new OAR084ForbiddenFormatsInQueryCheck();
        v2Path = getV2Path("security");
        v3Path = getV3Path("security");
    }

    @Test
    public void verifyInV2() {
        verifyV2("valid-query-formats");
    }

    @Test
    public void verifyInV2ForbiddenQueryFormats() {
        verifyV2("forbidden-query-formats");
    }

    @Test
    public void verifyInV3() {
        verifyV3("valid-query-formats");
    }

    @Test
    public void verifyInV3ForbiddenQueryFormats() {
        verifyV3("forbidden-query-formats");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("OAR084 - ForbiddenQueryFormats - Some formats should not pass through this querystring", RuleType.VULNERABILITY, Severity.MAJOR, tags("safety"));
    }

    @Override
    public void verifyParameters() {
        assertNumberOfParameters(3);
        assertParameterProperties("forbidden-query-formats", "password", RuleParamType.STRING);
        assertParameterProperties("paths", "/examples", RuleParamType.STRING);
        assertParameterProperties("pathValidationStrategy", "/include", RuleParamType.STRING);
    }
}