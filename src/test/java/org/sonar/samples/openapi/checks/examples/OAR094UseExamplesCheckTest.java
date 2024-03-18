package org.sonar.samples.openapi.checks.examples;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.openapi.BaseCheckTest;

import apiquality.sonar.openapi.checks.examples.OAR094UseExamplesCheck;

public class OAR094UseExamplesCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "OAR094";
        check = new OAR094UseExamplesCheck();
        v2Path = getV2Path("examples");
        v3Path = getV3Path("examples");
    }
    @Test
    public void verifyInV2() {
        verifyV2("invalid-example");
    }
    @Test
    public void verifyvalidV2() {
        verifyV2("valid-example");
    }
    @Test
    public void verifyInV3() {
        verifyV3("invalid-example");
    }
    @Test
    public void verifyvalidV3() {
        verifyV3("valid-example");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("OAR094 - UseExamples - It is recommended to use examples instead of example as some tools like microcks use this section of the definition", RuleType.BUG, Severity.MINOR, tags("examples"));
    }
}
