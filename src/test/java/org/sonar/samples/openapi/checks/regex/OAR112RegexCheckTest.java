package org.sonar.samples.openapi.checks.regex;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.api.server.rule.RuleParamType;
import org.sonar.samples.openapi.BaseCheckTest;

import apiquality.sonar.openapi.checks.regex.OAR112RegexCheck;


public class OAR112RegexCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "OAR112";
        check = new OAR112RegexCheck();
        v2Path = getV2Path("regex");
        v3Path = getV3Path("regex");
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
        assertRuleProperties("OAR112 - TemplateRule - Create your own OpenAPI rules", RuleType.BUG, Severity.MINOR, tags("regex"));
    }

    @Override
    public void verifyParameters() {
        assertNumberOfParameters(3);
        assertParameterProperties("Node", "info/description", RuleParamType.STRING);
        assertParameterProperties("Error Message", "The field must start with an uppercase letter.", RuleParamType.STRING);
        assertParameterProperties("Validation", "^[A-Z].*", RuleParamType.STRING);
    }
}