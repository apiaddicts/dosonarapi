package org.sonar.samples.openapi;

import org.junit.BeforeClass;
import org.junit.Test;
import org.sonar.api.rules.RuleType;
import org.sonar.api.server.rule.RuleParamType;
import org.sonar.api.server.rule.RulesDefinition;

import apiquality.sonar.openapi.I18nContext;
import apiquality.sonar.openapi.OpenAPICustomRulesDefinition;

import org.apiaddicts.apitools.dosonarapi.api.OpenApiCheck;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class BaseCheckTest {

    private static final Logger LOG = Logger.getGlobal();

    protected static RulesDefinition.Repository repository;

    protected String ruleName;
    protected OpenApiCheck check;
    protected String v2Path;
    protected String v3Path;

    protected String getV2Path(String prefix) {
        return "src/test/resources/checks/v2/" + prefix + "/" + ruleName + "/";
    }

    protected String getV3Path(String prefix) {
        return "src/test/resources/checks/v3/" + prefix + "/" + ruleName + "/";
    }

    protected void verifyV2(String file) {
        verify(file, true);
    }

    protected void verifyV3(String file) {
        verify(file, false);
    }

    private void verify(String file, boolean isV2) {
        file = (isV2 ? v2Path : v3Path) + file;
        if (file.contains(".")) {
            LOG.info("Testing file : " + file);
            ExtendedOpenApiCheckVerifier.verify(file, check, isV2);
        } else {
            LOG.info("Testing file : " + file + ".yaml");
            ExtendedOpenApiCheckVerifier.verify(file + ".yaml", check, isV2);
            LOG.info("Testing file : " + file + ".json");
            ExtendedOpenApiCheckVerifier.verify(file + ".json", check, isV2);
        }
    }

    @BeforeClass
    public static void beforeClass() {
        I18nContext.setLang("en");
        if (repository != null) return;
        OpenAPICustomRulesDefinition rulesDefinition = new OpenAPICustomRulesDefinition();
        RulesDefinition.Context context = new RulesDefinition.Context();
        rulesDefinition.define(context);
        repository = context.repository(OpenAPICustomRulesDefinition.REPOSITORY_KEY);
    }

    @Test
    public abstract void verifyRule();

    @Test
    public void verifyParameters() {
        assertNumberOfParameters(0);
    }

    protected void assertRuleProperties(String title, RuleType type, String severity, Set<String> tags) {
        RulesDefinition.Rule rule = repository.rule(ruleName);
        assertThat(rule).isNotNull();
        assertThat(rule.name()).isEqualTo(title);
        assertThat(rule.type()).isEqualTo(type);
        assertThat(rule.severity()).isEqualTo(severity);
        assertThat(rule.tags()).isEqualTo(tags);
    }

    protected Set<String> tags(String... tags) {
        return new HashSet<>(Arrays.asList(tags));
    }

    protected void assertParameterProperties(String paramName, String defaultValue, RuleParamType type) {
        RulesDefinition.Param param = repository.rule(ruleName).param(paramName);
        assertThat(param).isNotNull();
        assertThat(param.defaultValue()).isEqualTo(defaultValue);
        assertThat(param.type()).isEqualTo(type);
    }

    protected void assertNumberOfParameters(int numberOfParameters) {
        List<RulesDefinition.Param> params = repository.rule(ruleName).params();
        assertThat(params.size()).isEqualTo(numberOfParameters);
    }
}
