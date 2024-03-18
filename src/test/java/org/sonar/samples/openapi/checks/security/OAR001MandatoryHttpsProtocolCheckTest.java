package org.sonar.samples.openapi.checks.security;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.openapi.BaseCheckTest;

import apiquality.sonar.openapi.checks.security.OAR001MandatoryHttpsProtocolCheck;

public class OAR001MandatoryHttpsProtocolCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "OAR001";
        check = new OAR001MandatoryHttpsProtocolCheck();
        v2Path = getV2Path("security");
        v3Path = getV3Path("security");
    }

    @Test
    public void verifyInV2WithSchemes() {
        verifyV2("with-schemes");
    }

    @Test
    public void verifyInV2WithoutSchemes() {
        verifyV2("without-schemes");
    }

    @Test
    public void verifyInV3WithServers() {
        verifyV3("with-servers");
    }

    @Test
    public void verifyInV3WithoutServers() {
        verifyV3("without-servers");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("OAR001 - MandatoryHttpsProtocol - Https protocol is mandatory", RuleType.VULNERABILITY, Severity.CRITICAL, tags("safety"));
    }
}
