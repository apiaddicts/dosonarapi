package org.sonar.samples.openapi.checks.format;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.openapi.BaseCheckTest;

import apiquality.sonar.openapi.checks.format.OAR007UndefinedResponseMediaTypeCheck;

public class OAR007UndefinedResponseMediaTypeCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "OAR007";
        check = new OAR007UndefinedResponseMediaTypeCheck();
        v2Path = getV2Path("format");
        v3Path = getV3Path("format");
    }

    @Test
    public void verifyInV2WithDefault() {
        verifyV2("with-default");
    }

    @Test
    public void verifyInV2WithSpecific() {
        verifyV2("with-specific");
    }

    @Test
    public void verifyInV2WithDefaultAndSpecific() {
        verifyV2("with-default-and-specific");
    }

    @Test
    public void verifyInV2WithoutAnything() {
        verifyV2("without-anything");
    }

    @Test
    public void verifyInV3WithoutAnything() {
        verifyV3("without-anything");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("OAR007 - UndefinedResponseMediaType - APIs must define response media types supported by the API", RuleType.BUG, Severity.BLOCKER, tags("format"));
    }
}
