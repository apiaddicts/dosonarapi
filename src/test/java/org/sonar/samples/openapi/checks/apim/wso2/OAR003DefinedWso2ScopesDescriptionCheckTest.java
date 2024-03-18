package org.sonar.samples.openapi.checks.apim.wso2;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.openapi.BaseCheckTest;

import apiquality.sonar.openapi.checks.apim.wso2.OAR003DefinedWso2ScopesDescriptionCheck;

public class OAR003DefinedWso2ScopesDescriptionCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "OAR003";
        check = new OAR003DefinedWso2ScopesDescriptionCheck();
        v2Path = getV2Path("apim/wso2");
        v3Path = getV3Path("apim/wso2");
    }

    @Test
    public void verifyInV2WithScopeDescription() {
        verifyV2("with-description");
    }

    @Test
    public void verifyInV2WithoutScopeDescription() {
        verifyV2("without-description");
    }

    @Test
    public void verifyInV2WithNullScopeDescription() {
        verifyV2("with-null-description");
    }

    @Test
    public void verifyInV3WithScopeDescription() {
        verifyV3("with-description");
    }

    @Test
    public void verifyInV3WithoutScopeDescription() {
        verifyV3("without-description");
    }

    @Test
    public void verifyInV3WithNullScopeDescription() {
        verifyV3("with-null-description");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("OAR003 - DefinedWso2ScopesDescription - WSO2 scope description is recommended", RuleType.VULNERABILITY, Severity.BLOCKER, tags("api-manager", "vulnerability", "wso2"));
    }
}
