package org.sonar.samples.openapi.checks.parameters;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.api.server.rule.RuleParamType;
import org.sonar.samples.openapi.BaseCheckTest;

import apiquality.sonar.openapi.checks.parameters.OAR022OrderbyParameterCheck;

public class OAR022OrderbyParameterCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "OAR022";
        check = new OAR022OrderbyParameterCheck();
        v2Path = getV2Path("parameters");
        v3Path = getV3Path("parameters");
    }

    @Test
    public void verifyInV2() {
        verifyV2("plain");
    }

    @Test
    public void verifyInV2Excluded() {
        verifyV2("excluded");
    }

    @Test
    public void verifyInV2Without() {
        verifyV2("plain-without");
    }
    @Test
    public void verifyInV3() {
        verifyV3("plain");
    }

    @Test
    public void verifyInV3Excluded() {
        verifyV3("excluded");
    }

    @Test
    public void verifyInV3Without() {
        verifyV3("plain-without");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("OAR022 - OrderbyParameter - the chosen parameter must be defined in this operation", RuleType.BUG, Severity.MINOR, tags("parameters"));
    }

    @Override
    public void verifyParameters() {
        assertNumberOfParameters(3);
        assertParameterProperties("paths", "/examples", RuleParamType.STRING);
        assertParameterProperties("pathValidationStrategy", "/include", RuleParamType.STRING);
        assertParameterProperties("parameterName", "$orderby", RuleParamType.STRING);
    }
}