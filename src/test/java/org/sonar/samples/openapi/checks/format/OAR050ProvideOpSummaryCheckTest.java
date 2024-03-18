package org.sonar.samples.openapi.checks.format;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.openapi.BaseCheckTest;

import apiquality.sonar.openapi.checks.format.OAR050ProvideOpSummaryCheck;

public class OAR050ProvideOpSummaryCheckTest extends BaseCheckTest {


    @Before
    public void init() {
        ruleName = "OAR050";
        check = new OAR050ProvideOpSummaryCheck();
        v2Path = getV2Path("format");
        v3Path = getV3Path("format");
    }

    @Test
    public void verifyInV2() {
        verifyV2("provide-summary");
    }

    @Test
    public void verifyInV3() {
        verifyV3("provide-summary");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("OAR050 - ProvideOpSummary - Provide a summary for each operation.", RuleType.BUG, Severity.BLOCKER, tags("format"));
    }
}
