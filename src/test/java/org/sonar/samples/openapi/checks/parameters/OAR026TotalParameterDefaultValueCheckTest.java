package org.sonar.samples.openapi.checks.parameters;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.openapi.BaseCheckTest;

import apiquality.sonar.openapi.checks.parameters.OAR026TotalParameterDefaultValueCheck;

public class OAR026TotalParameterDefaultValueCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "OAR026";
        check = new OAR026TotalParameterDefaultValueCheck();
        v2Path = getV2Path("parameters");
        v3Path = getV3Path("parameters");
    }

    @Test
    public void verifyInV2With$totalWithDefvalFalse() {
        verifyV2("plain-with-$total-with-defval-false");
    }

    @Test
    public void verifyInV2With$totalWithDefvalTrue() {
        verifyV2("plain-with-$total-with-defval-true");
    }

    @Test
    public void verifyInV2With$totalWithoutDefval() {
        verifyV2("plain-with-$total-without-defval");
    }

    @Test
    public void verifyInV2Without$total() {
        verifyV2("plain-without-$total");
    }

    @Test
    public void verifyInV2With$refWith$totalWithDefvalFalse() {
        verifyV2("with-$ref-with-$total-with-defval-false");
    }

    @Test
    public void verifyInV2With$refWith$totalWithDefvalTrue() {
        verifyV2("with-$ref-with-$total-with-defval-true");
    }

    @Test
    public void verifyInV2With$refWith$totalWithoutDefval() {
        verifyV2("with-$ref-with-$total-without-defval");
    }

    @Test
    public void verifyInV2With$refWithout$total() {
        verifyV2("with-$ref-without-$total");
    }

    @Test
    public void verifyInV2WithoutParameters() {
        verifyV2("without-parameters");
    }

    @Test
    public void verifyInV3With$totalWithDefvalFalse() {
        verifyV3("plain-with-$total-with-defval-false");
    }

    @Test
    public void verifyInV3With$totalWithDefvalTrue() {
        verifyV3("plain-with-$total-with-defval-true");
    }

    @Test
    public void verifyInV3With$totalWithoutDefval() {
        verifyV3("plain-with-$total-without-defval");
    }

    @Test
    public void verifyInV3Without$total() {
        verifyV3("plain-without-$total");
    }

    @Test
    public void verifyInV3With$refWith$totalWithDefvalFalse() {
        verifyV3("with-$ref-with-$total-with-defval-false");
    }

    @Test
    public void verifyInV3With$refWith$totalWithDefvalTrue() {
        verifyV3("with-$ref-with-$total-with-defval-true");
    }

    @Test
    public void verifyInV3With$refWith$totalWithoutDefval() {
        verifyV3("with-$ref-with-$total-without-defval");
    }

    @Test
    public void verifyInV3With$refWithout$total() {
        verifyV3("with-$ref-without-$total");
    }

    @Test
    public void verifyInV3WithoutParameters() {
        verifyV3("without-parameters");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("OAR026 - TotalParameterDefaultValue - The $total parameter default value should be false", RuleType.BUG, Severity.CRITICAL, tags("parameters"));
    }

}
