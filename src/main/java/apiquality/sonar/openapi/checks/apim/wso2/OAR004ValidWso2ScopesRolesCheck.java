package apiquality.sonar.openapi.checks.apim.wso2;

import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

import java.util.regex.Pattern;

@Rule(key = OAR004ValidWso2ScopesRolesCheck.KEY)
public class OAR004ValidWso2ScopesRolesCheck extends AbstractWso2ScopesCheck {

	public static final String KEY = "OAR004";
	private static final String DEFAULT_PATTERN_VALUE = "^[a-zA-Z0-9_\\-., ]+$";
	private static final String MESSAGE = "OAR004.error";

	@RuleProperty(
			key = "pattern",
			description = "Regular expression used to check the 'roles' field against.",
			defaultValue = DEFAULT_PATTERN_VALUE)
	public String patternStr = DEFAULT_PATTERN_VALUE;

	private Pattern pattern;

	@Override
	protected void visitFile(JsonNode root) {
		pattern = Pattern.compile(patternStr);
	}

	@Override
	protected void visitScope(JsonNode scope) {
		JsonNode roles = scope.propertyMap().get("roles");
		if (roles == null || roles.isNull() || roles.isMissing()) return;
		String rolesText = roles.getTokenValue();
		boolean notValid = !pattern.matcher(rolesText).matches();
		if (notValid) addIssue(KEY, translate(MESSAGE), roles);
	}
}
