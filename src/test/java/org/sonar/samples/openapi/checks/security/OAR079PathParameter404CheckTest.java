package org.sonar.samples.openapi.checks.security;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.api.server.rule.RuleParamType;
import org.sonar.samples.openapi.BaseCheckTest;

import apiquality.sonar.openapi.checks.security.OAR079PathParameter404Check;

public class OAR079PathParameter404CheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "OAR079";
        check = new OAR079PathParameter404Check();
        v2Path = getV2Path("security");
        v3Path = getV3Path("security");
    }

    @Test
    public void verifyInV2BadRequest() {
        verifyV2("bad-request404");
    }

    @Test
    public void verifyInV2NoBadRequest() {
        verifyV2("no-bad-request404");
    }

    @Test
    public void verifyInV3BadRequest() {
        verifyV3("bad-request404");
    }

    @Test
    public void verifyInV3NoBadRequest() {
        verifyV3("no-bad-request404");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("OAR079 - PathParameter404 - Paths parameters, should have not found (404) response",
                RuleType.VULNERABILITY, Severity.MINOR, tags("safety"));
    }

    @Override
    public void verifyParameters() {
        assertNumberOfParameters(2);
        assertParameterProperties("paths", "/status", RuleParamType.STRING);
        assertParameterProperties("pathValidationStrategy", "/exclude", RuleParamType.STRING);
    }
}