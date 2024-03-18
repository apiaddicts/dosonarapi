package org.sonar.samples.openapi.checks.format;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.openapi.BaseCheckTest;

import apiquality.sonar.openapi.checks.format.OAR067CamelCaseNamingConventionCheck;

public class OAR067CamelCaseNamingConventionCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "OAR067";
        check = new OAR067CamelCaseNamingConventionCheck();
        v2Path = getV2Path("format");
        v3Path = getV3Path("format");
    }

    @Test
    public void verifyInV2() {
        verifyV2("camel-case-error");
    }
    @Test
    public void verifyvalidV2() {
        verifyV3("valid");
    }

    @Test
    public void verifyInV3() {
        verifyV3("camel-case-error");
    }
    @Test
    public void verifyvalidV3() {
        verifyV3("valid");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("OAR067 - CamelCaseNamingConvention - RequestBody and Responses schema property names must be compliant with the camelCase naming convention", RuleType.BUG, Severity.MINOR, tags("format"));
    }
}