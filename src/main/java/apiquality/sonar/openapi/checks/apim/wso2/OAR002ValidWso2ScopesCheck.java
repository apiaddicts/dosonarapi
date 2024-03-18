package apiquality.sonar.openapi.checks.apim.wso2;

import org.sonar.check.Rule;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

import java.util.List;
import java.util.Map;

@Rule(key = OAR002ValidWso2ScopesCheck.KEY)
public class OAR002ValidWso2ScopesCheck extends AbstractWso2ScopesCheck {

	public static final String KEY = "OAR002";
	private static final String MESSAGE = "OAR002.error";
	private static final String MESSAGE_PROP = "OAR002.error-property";

	private JsonNode scopesNode;

	@Override
	protected void visitScopesNode(JsonNode scopesNode) {
		if (scopesNode.isNull()) addIssue(KEY, translate(MESSAGE), scopesNode.key());
		this.scopesNode = scopesNode;
	}

	@Override
	protected void visitScopes(List<JsonNode> scopes) {
		if (scopes.isEmpty()) addIssue(KEY, translate(MESSAGE), scopesNode.key());
	}

	@Override
	protected void visitScope(JsonNode scope) {
		Map<String, JsonNode> props = scope.propertyMap();
		validateProperty(props, "name", scope);
		validateProperty(props, "key", scope);
		validateProperty(props, "roles", scope);
	}

	private void validateProperty(Map<String, JsonNode> properties, String propertyName, JsonNode scope) {
		JsonNode property = properties.get(propertyName);
		if (!properties.containsKey(propertyName)) {
			addIssue(KEY, translate(MESSAGE_PROP, propertyName), scope);
		} else if(property.isNull() || property.getTokenValue().trim().equals("")) {
			addIssue(KEY, translate(MESSAGE_PROP, propertyName), property.key());
		}
	}
}
