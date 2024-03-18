package org.sonar.samples.openapi.checks.apim.wso2;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.api.server.rule.RuleParamType;
import org.sonar.samples.openapi.BaseCheckTest;

import apiquality.sonar.openapi.checks.apim.wso2.OAR004ValidWso2ScopesRolesCheck;

public class OAR004ValidWso2ScopesRolesCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "OAR004";
        check = new OAR004ValidWso2ScopesRolesCheck();
        v2Path = getV2Path("apim/wso2");
        v3Path = getV3Path("apim/wso2");
    }

    @Test
    public void verifyInV2WithValidRoles() {
        verifyV2("with-valid-roles");
    }

    @Test
    public void verifyInV2WithInvalidRoles() {
        verifyV2("with-invalid-roles");
    }

    @Test
    public void verifyInV3WithValidRoles() {
        verifyV3("with-valid-roles");
    }

    @Test
    public void verifyInV3WithInvalidRoles() {
        verifyV3("with-invalid-roles");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("OAR004 - ValidWso2ScopesRoles - WSO2 scope roles value is not valid", RuleType.VULNERABILITY, Severity.BLOCKER, tags("api-manager", "vulnerability", "wso2"));
    }

    @Override
    public void verifyParameters() {
        assertNumberOfParameters(1);
        assertParameterProperties("pattern", "^[a-zA-Z0-9_\\-., ]+$", RuleParamType.STRING);
    }
}
