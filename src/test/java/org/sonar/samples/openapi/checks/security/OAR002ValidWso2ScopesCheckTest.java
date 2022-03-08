package org.sonar.samples.openapi.checks.security;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.openapi.BaseCheckTest;

public class OAR002ValidWso2ScopesCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "OAR002";
        check = new OAR002ValidWso2ScopesCheck();
        v2Path = getV2Path("security");
        v3Path = getV3Path("security");
    }

    @Test
    public void verifyInV2WithScopes() {
        verifyV2("with-scopes");
    }

    @Test
    public void verifyInV2WithNullScopes() {
        verifyV2("with-null-scopes");
    }

    @Test
    public void verifyInV2WithEmptyScopes() {
        verifyV2("with-empty-scopes");
    }

    @Test
    public void verifyInV2WithoutScopes() {
        verifyV2("without-scopes");
    }

    @Test
    public void verifyInV2WithoutSecurity() {
        verifyV2("without-security");
    }

    @Test
    public void verifyInV3WithScopes() {
        verifyV3("with-scopes");
    }

    @Test
    public void verifyInV3WithNullScopes() {
        verifyV3("with-null-scopes");
    }

    @Test
    public void verifyInV3WithEmptyScopes() {
        verifyV3("with-empty-scopes");
    }

    @Test
    public void verifyInV3WithoutScopes() {
        verifyV3("without-scopes");
    }

    @Test
    public void verifyInV3WithoutSecurity() {
        verifyV3("without-security");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("OAR002 - ValidWso2Scopes - WSO2 scope definition is wrong", RuleType.BUG, Severity.BLOCKER, tags("vulnerability"));
    }
}
