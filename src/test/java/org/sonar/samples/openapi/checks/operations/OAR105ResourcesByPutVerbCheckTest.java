package org.sonar.samples.openapi.checks.operations;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.api.server.rule.RuleParamType;
import org.sonar.samples.openapi.BaseCheckTest;

import apiquality.sonar.openapi.checks.operations.OAR105ResourcesByPutVerbCheck;

public class OAR105ResourcesByPutVerbCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "OAR105";
        check = new OAR105ResourcesByPutVerbCheck();
        v2Path = getV2Path("operations");
        v3Path = getV3Path("operations");
    }

    @Test
    public void verifyInV2() {
        verifyV2("plain");
    }

    @Test
    public void verifyInV3() {
        verifyV3("plain");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("OAR105 - ResourcesByPutVerb - Put Operation not recommended for resource path", RuleType.BUG, Severity.MAJOR, tags("operations"));
    }

    @Override
    public void verifyParameters() {
        assertNumberOfParameters(1);
        assertParameterProperties("words-to-exclude", "get,delete", RuleParamType.STRING);
    }
}