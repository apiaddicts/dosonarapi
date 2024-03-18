package org.sonar.samples.openapi.checks.security;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.api.server.rule.RuleParamType;
import org.sonar.samples.openapi.BaseCheckTest;

import apiquality.sonar.openapi.checks.security.OAR082BinaryOrByteFormatCheck;

public class OAR082BinaryOrByteFormatCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "OAR082";
        check = new OAR082BinaryOrByteFormatCheck();
        v2Path = getV2Path("security");
        v3Path = getV3Path("security");
    }
    @Test
    public void verifyvalidV2() {
        verifyV2("valid-format");
    }
    @Test
    public void verifyvalidV3() {
        verifyV3("valid-format");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("OAR082 - BinaryOrByte - The string properties of the specified parameters must define a byte or binary format.", RuleType.VULNERABILITY, Severity.MAJOR, tags("safety"));
    }
    @Override
    public void verifyParameters() {
        assertNumberOfParameters(1);
        assertParameterProperties("fields-to-apply", "product,line,price", RuleParamType.STRING);
    }
}
