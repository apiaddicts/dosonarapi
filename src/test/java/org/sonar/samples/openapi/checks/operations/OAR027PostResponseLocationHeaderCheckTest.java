package org.sonar.samples.openapi.checks.operations;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.samples.openapi.BaseCheckTest;

import apiquality.sonar.openapi.checks.operations.OAR027PostResponseLocationHeaderCheck;

public class OAR027PostResponseLocationHeaderCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "OAR027";
        check = new OAR027PostResponseLocationHeaderCheck();
        v2Path = getV2Path("operations");
        v3Path = getV3Path("operations");
    }

    @Test
    public void verifyInV2NoPost() {
        verifyV2("no-post");
    }

    @Test
    public void verifyInV2PostNo201() {
        verifyV2("post-no-201");
    }

    @Test
    public void verifyInV2Post201WithoutLocation() {
        verifyV2("post-201-without-location");
    }

    @Test
    public void verifyInV2Post201WithOtherHeaders() {
        verifyV2("post-201-with-other-headers");
    }

    @Test
    public void verifyInV2Post201WithLocation() {
        verifyV2("post-201-with-location");
    }

    @Test
    public void verifyInV3NoPost() {
        verifyV3("no-post");
    }

    @Test
    public void verifyInV3PostNo201() {
        verifyV3("post-no-201");
    }

    @Test
    public void verifyInV3Post201WithoutLocation() {
        verifyV3("post-201-without-location");
    }

    @Test
    public void verifyInV3Post201WithOtherHeaders() {
        verifyV3("post-201-with-other-headers");
    }

    @Test
    public void verifyInV3Post201WithLocation() {
        verifyV3("post-201-with-location");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("OAR027 - PostResponseLocationHeader - Location header is required in responses with code 201 from POST operations", RuleType.BUG, Severity.MAJOR, tags("operations"));
    }

}
