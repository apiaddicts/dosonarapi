package org.sonar.samples.openapi.checks.apim.wso2;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.openapi.BaseCheckTest;

import apiquality.sonar.openapi.checks.apim.wso2.OAR005UndefinedWso2ScopeUseCheck;

public class OAR005UndefinedWso2ScopeUseCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "OAR005";
        check = new OAR005UndefinedWso2ScopeUseCheck();
        v2Path = getV2Path("apim/wso2");
        v3Path = getV3Path("apim/wso2");
    }

    @Test
    public void verifyInV2WithNullOperationScope() {
        verifyV2("with-null-operation-scope");
    }

    @Test
    public void verifyInV2WithWrongOperationScope() {
        verifyV2("with-wrong-operation-scope");
    }

    @Test
    public void verifyInV2WithCorrectOperationScope() {
        verifyV2("with-correct-operation-scope");
    }

    @Test
    public void verifyInV3WithNullOperationScope() {
        verifyV3("with-null-operation-scope");
    }

    @Test
    public void verifyInV3WithWrongOperationScope() {
        verifyV3("with-wrong-operation-scope");
    }

    @Test
    public void verifyInV3WithCorrectOperationScope() {
        verifyV3("with-correct-operation-scope");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("OAR005 - UndefinedWso2ScopeUse - WSO2 scope definition does not exists", RuleType.VULNERABILITY, Severity.BLOCKER, tags("api-manager", "vulnerability", "wso2"));
    }
}
