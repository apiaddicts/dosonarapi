package org.sonar.samples.openapi.checks.format;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.openapi.BaseCheckTest;
import apiquality.sonar.openapi.checks.format.OAR099ApiPrefixBasePathCheck;

public class OAR099ApiPrefixBasePathCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "OAR099";
        check = new OAR099ApiPrefixBasePathCheck();
        v2Path = getV2Path("format");
        v3Path = getV3Path("format");
    }

    @Test
    public void verifyInV2() {
        verifyV2("valid");
    }

    @Test
    public void verifyInV2WithoutApiPrefix() {
        verifyV2("without-api-prefix");
    }

    @Test
    public void verifyInV3() {
        verifyV3("valid");
    }

    @Test
    public void verifyInV3WithoutApiPrefix() {
        verifyV3("without-api-prefix");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("OAR099 - ApiPrefixBasePath - API name must start with the prefix 'api-'", RuleType.BUG, Severity.CRITICAL, tags("format"));
    }

    @Override
    public void verifyParameters() {
        assertNumberOfParameters(0);
    }
}