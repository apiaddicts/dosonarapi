package org.sonar.samples.openapi.checks.apim.wso2;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.api.server.rule.RuleParamType;
import org.sonar.samples.openapi.BaseCheckTest;

import apiquality.sonar.openapi.checks.apim.wso2.OAR040StandardWso2ScopesNameCheck;

public class OAR040StandardWso2ScopesNameCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "OAR040";
        check = new OAR040StandardWso2ScopesNameCheck();
        v2Path = getV2Path("apim/wso2");
        v3Path = getV3Path("apim/wso2");
    }

    @Test
    public void verifyInV2WithValidNames() {
        verifyV2("valid");
    }

    @Test
    public void verifyInV2WithInvalidNames() {
        verifyV2("invalid");
    }

    @Test
    public void verifyInV3WithValidNames() {
        verifyV3("valid");
    }

    @Test
    public void verifyInV3WithInvalidNames() {
        verifyV3("invalid");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("OAR040 - StandardWso2ScopesName - WSO2 scope name is non compliant with the standard", RuleType.VULNERABILITY, Severity.BLOCKER, tags("api-manager", "vulnerability", "wso2"));
    }

    @Override
    public void verifyParameters() {
        assertNumberOfParameters(1);
        assertParameterProperties("pattern", "^[a-zA-Z]{4,}_(SC|sc)_[a-zA-Z0-9]{1,}$", RuleParamType.STRING);
    }
}
