package org.sonar.samples.openapi.checks.operations;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.api.server.rule.RuleParamType;
import org.sonar.samples.openapi.BaseCheckTest;

import apiquality.sonar.openapi.checks.operations.OAR071GetQueryParamsDefinedCheck;

public class OAR071GetQueryParamsDefinedCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "OAR071";
        check = new OAR071GetQueryParamsDefinedCheck();
        v2Path = getV2Path("operations");
        v3Path = getV3Path("operations");
    }

    @Test
    public void verifyInV2() {
        verifyV2("valid-query-params");
    }

    @Test
    public void verifyInV2MissingQueryParams() {
        verifyV2("missing-query-params");
    }

    @Test
    public void verifyInV3() {
        verifyV3("valid-query-params");
    }

    @Test
    public void verifyInV3MissingQueryParams() {
        verifyV3("missing-query-params");
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("OAR071 - GetQueryParamsDefined - Query parameters in GET operations must be defined according to the standard", RuleType.BUG, Severity.MAJOR, tags("operations"));
    }

    @Override
    public void verifyParameters() {
        assertNumberOfParameters(3);
        assertParameterProperties("mandatory-query-params", "param1, param2, param3", RuleParamType.STRING);
        assertParameterProperties("paths", "/examples", RuleParamType.STRING);
        assertParameterProperties("pathValidationStrategy", "/include", RuleParamType.STRING);
    }
}