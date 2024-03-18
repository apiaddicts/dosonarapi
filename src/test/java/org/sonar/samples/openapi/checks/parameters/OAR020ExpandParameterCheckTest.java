package org.sonar.samples.openapi.checks.parameters;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.api.server.rule.RuleParamType;
import org.sonar.samples.openapi.BaseCheckTest;

import apiquality.sonar.openapi.checks.parameters.OAR020ExpandParameterCheck;

public class OAR020ExpandParameterCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "OAR020";
        check = new OAR020ExpandParameterCheck();
        v2Path = getV2Path("parameters");
        v3Path = getV3Path("parameters");
    }

    @Test
    public void verifyInV2() {
        verifyV2("plain2");
    }

    @Test
    public void verifyInV2Excluded() {
        verifyV2("excluded2");
    }

    @Test
    public void verifyInV2Without() {
        verifyV2("plain-without2");
    }

    @Test
    public void verifyInV2WithRef() {
        verifyV2("with-ref");
    }

    @Test
    public void verifyInV3() {
        verifyV3("plain2");
    }

    @Test
    public void verifyInV3Excluded() {
        verifyV3("excluded2");
    }

    @Test
    public void verifyInV3Without() {
        verifyV3("plain-without2");
    }

    @Test
    public void verifyInV3WithRef() {
        verifyV3("with-ref");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("OAR020 - ExpandParameter - the chosen parameter must be defined in this operation", RuleType.BUG, Severity.MINOR, tags("parameters"));
    }

    @Override
    public void verifyParameters() {
        assertNumberOfParameters(3);
        assertParameterProperties("paths", "/examples", RuleParamType.STRING);
        assertParameterProperties("pathValidationStrategy", "/include", RuleParamType.STRING);
        assertParameterProperties("parameterName", "$expand", RuleParamType.STRING);
    }
}