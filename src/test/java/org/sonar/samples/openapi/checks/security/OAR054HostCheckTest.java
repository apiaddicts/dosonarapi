package org.sonar.samples.openapi.checks.security;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.api.server.rule.RuleParamType;
import org.sonar.samples.openapi.BaseCheckTest;

import apiquality.sonar.openapi.checks.security.OAR054HostCheck;

public class OAR054HostCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "OAR054";
        check = new OAR054HostCheck();
        v2Path = getV2Path("security");
        v3Path = getV3Path("security");
    }

    @Test
    public void verifyInV2() {
        verifyV2("valid");
    }

    @Test
    public void verifyInV3() {
        verifyV3("valid");
    }

    @Override
    public void verifyParameters() {
        assertNumberOfParameters(1);
        assertParameterProperties("host-regex", "^((\\*|[\\w\\d]+(-[\\w\\d]+)*)\\.)*(apiaddicts)(\\.org)$", RuleParamType.STRING);
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("OAR054 - Host - Host must be compliant with the standard", RuleType.VULNERABILITY, Severity.BLOCKER, tags("safety"));
    }
}
