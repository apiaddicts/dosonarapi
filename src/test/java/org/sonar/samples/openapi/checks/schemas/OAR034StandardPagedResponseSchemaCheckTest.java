package org.sonar.samples.openapi.checks.schemas;

import org.junit.Before;
import org.junit.Test;
import org.sonar.api.rule.Severity;
import org.sonar.api.rules.RuleType;
import org.sonar.api.server.rule.RuleParamType;
import org.sonar.samples.openapi.BaseCheckTest;

import apiquality.sonar.openapi.checks.schemas.OAR034StandardPagedResponseSchemaCheck;

public class OAR034StandardPagedResponseSchemaCheckTest extends BaseCheckTest {

    @Before
    public void init() {
        ruleName = "OAR034";
        check = new OAR034StandardPagedResponseSchemaCheck();
        v2Path = getV2Path("schemas");
        v3Path = getV3Path("schemas");
    }

    @Test
    public void verifyInV2() {
        verifyV2("valid");
    }

    @Test
    public void verifyInV2AllOf() {
        verifyV2("valid-all-of");
    }

    @Test
    public void verifyInV2WithoutPaging() {
        verifyV2("without-paging");
    }

    @Test
    public void verifyInV2WithPagingWrongType() {
        verifyV2("with-paging-wrong-type");
    }

    @Test
    public void verifyInV2WithPagingWithoutProperties() {
        verifyV2("with-paging-without-properties");
    }

    @Test
    public void verifyInV2WithPagingWithPropertiesWrongTypes() {
        verifyV2("with-paging-with-properties-wrong-types");
    }

    @Test
    public void verifyInV2WithLinksWithoutProperties() {
        verifyV2("with-links-without-properties");
    }

    @Test
    public void verifyInV2WithoutLinksRequiredFields() {
        verifyV2("without-links-required-fields");
    }

    @Test
    public void verifyInV2WithoutPaginationRequiredFields() {
        verifyV2("without-pagination-required-fields");
    }

    @Test
    public void verifyInV3() {
        verifyV3("valid");
    }

    @Test
    public void verifyInV3AllOf() {
        verifyV3("valid-all-of");
    }

    @Test
    public void verifyInV3WithoutPaging() {
        verifyV3("without-paging");
    }

    @Test
    public void verifyInV3WithPagingWrongType() {
        verifyV3("with-paging-wrong-type");
    }

    @Test
    public void verifyInV3WithPagingWithoutProperties() {
        verifyV3("with-paging-without-properties");
    }

    @Test
    public void verifyInV3WithPagingWithPropertiesWrongTypes() {
        verifyV3("with-paging-with-properties-wrong-types");
    }

    @Test
    public void verifyInV3WithLinksWithoutProperties() {
        verifyV3("with-links-without-properties");
    }

    @Test
    public void verifyInV3WithoutLinksRequiredFields() {
        verifyV3("without-links-required-fields");
    }

    @Test
    public void verifyInV3WithoutPaginationRequiredFields() {
        verifyV3("without-pagination-required-fields");
    }

    @Override
    public void verifyParameters() {
        assertNumberOfParameters(1);
        assertParameterProperties("paging-schema", "{\"type\":\"object\",\"properties\":{\"numPages\":{\"type\":\"integer\"},\"total\":{\"type\":\"integer\"},\"start\":{\"type\":\"integer\"},\"limit\":{\"type\":\"integer\"},\"links\":{\"type\":\"object\",\"properties\":{\"next\":{\"type\":\"object\",\"properties\":{\"href\":{\"type\":\"string\"}}},\"previous\":{\"type\":\"object\",\"properties\":{\"href\":{\"type\":\"string\"}}},\"last\":{\"type\":\"object\",\"properties\":{\"href\":{\"type\":\"string\"}}},\"self\":{\"type\":\"object\",\"properties\":{\"href\":{\"type\":\"string\"}}},\"first\":{\"type\":\"object\",\"properties\":{\"href\":{\"type\":\"string\"}}}},\"required\":[\"self\",\"previous\",\"next\"]}},\"required\":[\"start\",\"limit\",\"links\"],\"pagingPropertyName\":\"paging\"}", RuleParamType.STRING);
    }

    @Override
    public void verifyRule() {
        assertRuleProperties("OAR034 - StandardPagedResponse - Paged response schema must be compliant with the standard", RuleType.BUG, Severity.BLOCKER, tags("schemas"));
    }
}
