package apiquality.sonar.openapi.checks.apim.wso2;

import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

import java.util.regex.Pattern;

@Rule(key = OAR040StandardWso2ScopesNameCheck.KEY)
public class OAR040StandardWso2ScopesNameCheck extends AbstractWso2ScopesCheck {

	public static final String KEY = "OAR040";
	private static final String DEFAULT_PATTERN_VALUE = "^[a-zA-Z]{4,}_(SC|sc)_[a-zA-Z0-9]{1,}$";
	private static final String MESSAGE = "OAR040.error";

	@RuleProperty(
			key = "pattern",
			description = "Regular expression used to check the 'name' field against.",
			defaultValue = DEFAULT_PATTERN_VALUE)
	public String patternStr = DEFAULT_PATTERN_VALUE;

	private Pattern pattern;

	@Override
	protected void visitFile(JsonNode root) {
		pattern = Pattern.compile(patternStr);
	}

	@Override
	protected void visitScope(JsonNode scope) {
		JsonNode name = scope.propertyMap().get("name");
		if (name == null || name.isNull() || name.isMissing()) return;
		String nameText = name.getTokenValue();
		boolean notValid = !pattern.matcher(nameText).matches();
		if (notValid) addIssue(KEY, translate(MESSAGE), name.value());
	}
}
