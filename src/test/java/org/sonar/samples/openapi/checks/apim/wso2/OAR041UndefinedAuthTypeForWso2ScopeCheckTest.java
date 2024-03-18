package org.sonar.samples.openapi.checks.apim.wso2;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.openapi.BaseCheckTest;

import apiquality.sonar.openapi.checks.apim.wso2.OAR041UndefinedAuthTypeForWso2ScopeCheck;

public class OAR041UndefinedAuthTypeForWso2ScopeCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "OAR041";
        check = new OAR041UndefinedAuthTypeForWso2ScopeCheck();
        v2Path = getV2Path("apim/wso2");
        v3Path = getV3Path("apim/wso2");
    }

    @Test
    public void verifyInV2WithScopeAndAuth() {
        verifyV2("with-scope-and-auth");
    }

    @Test
    public void verifyInV2WithScopeWithoutAuth() {
        verifyV2("with-scope-without-auth");
    }

    @Test
    public void verifyInV2WithoutScopeAndAuth() {
        verifyV2("without-scope-and-auth");
    }

    @Test
    public void verifyInV3WithScopeAndAuth() {
        verifyV3("with-scope-and-auth");
    }

    @Test
    public void verifyInV3WithScopeWithoutAuth() {
        verifyV3("with-scope-without-auth");
    }

    @Test
    public void verifyInV3WithoutScopeAndAuth() {
        verifyV3("without-scope-and-auth");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("OAR041 - UndefinedAuthTypeForWso2Scope - Use of WSO2 x-scope requires x-auth-type definition", RuleType.VULNERABILITY, Severity.BLOCKER, tags("api-manager", "vulnerability", "wso2"));
    }
}
