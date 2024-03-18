package org.sonar.samples.openapi.checks.operations;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.api.server.rule.RuleParamType;
import org.sonar.samples.openapi.BaseCheckTest;

import apiquality.sonar.openapi.checks.operations.OAR038StandardCreateResponseCheck;

public class OAR038StandardCreateResponseCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "OAR038";
        check = new OAR038StandardCreateResponseCheck();
        v2Path = getV2Path("operations");
        v3Path = getV3Path("operations");
    }

    @Test
    public void verifyInV2WithOneProperty() {
        verifyV2("valid-one-property");
    }

    @Test
    public void verifyInV2WithMultipleProperties() {
        verifyV2("valid-multiple-properties");
    }

    @Test
    public void verifyInV2WithoutProperties() {
        verifyV2("without-properties");
    }

    @Test
    public void verifyInV2WithPropertiesEmpty() {
        verifyV2("with-properties-empty");
    }

    @Test
    public void verifyInV2WithoutData() {
        verifyV2("without-data");
    }

    @Test
    public void verifyInV2WithoutSchema() {
        verifyV2("without-schema");
    }

    @Test
    public void verifyInV3WithOneProperty() {
        verifyV3("valid-one-property");
    }

    @Test
    public void verifyInV3WithMultipleProperties() {
        verifyV3("valid-multiple-properties");
    }

    @Test
    public void verifyInV3WithoutProperties() {
        verifyV3("without-properties");
    }

    @Test
    public void verifyInV3WithPropertiesEmpty() {
        verifyV3("with-properties-empty");
    }

    @Test
    public void verifyInV3WithoutData() {
        verifyV3("without-data");
    }

    @Test
    public void verifyInV3WithoutSchema() {
        verifyV3("without-schema");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("OAR038 - StandardCreateResponse - Creation response schema must be compliant with the standard", RuleType.BUG, Severity.MAJOR, tags("operations"));
    }

    @Override
    public void verifyParameters() {
        assertNumberOfParameters(1);
        assertParameterProperties("data-property", "data", RuleParamType.STRING);
    }

}
