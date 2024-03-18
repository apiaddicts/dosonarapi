package apiquality.sonar.openapi;

import org.apiaddicts.apitools.dosonarapi.api.OpenApiCustomRuleRepository;
import org.sonar.api.server.profile.BuiltInQualityProfilesDefinition;
import org.sonar.api.utils.AnnotationUtils;
import org.sonar.check.Rule;
import apiquality.sonar.openapi.checks.RulesLists;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Declare a new quality profile that comprises all the custom rules, plus the SonarOpenApi standard rules.
 * <p>
 * This allows to create a built-in profile that extends the Sonar Way profile, and that includes your rules.
 * This profile will automatically inherit any new rule brought in by the core plugin.
 */
public class OpenAPICustomProfileDefinition implements BuiltInQualityProfilesDefinition {
	public static final String MY_COMPANY_WAY = "Custom";

	public OpenAPICustomProfileDefinition() {
		this(null);
	}

	public OpenAPICustomProfileDefinition(@Nullable OpenApiCustomRuleRepository[] repositories) {
	}

	@Override
	public void define(Context context) {
		NewBuiltInQualityProfile profile = context.createBuiltInQualityProfile(MY_COMPANY_WAY, "openapi");
		addRepositoryRules(profile, OpenAPICustomRulesDefinition.REPOSITORY_KEY, RulesLists.getAllChecks());
		profile.done();
	}

	private void addRepositoryRules(NewBuiltInQualityProfile profile, String key, List<Class<?>> checks) {
		for (Class<?> check : checks) {
			Rule annotation = AnnotationUtils.getAnnotation(check, Rule.class);
			if (!isTemplateRule(annotation.key())) {
				profile.activateRule(key, annotation.key());
			}
		}
	}
	
	private boolean isTemplateRule(String ruleKey) {
		return "OAR112".equals(ruleKey); 
	}
}
