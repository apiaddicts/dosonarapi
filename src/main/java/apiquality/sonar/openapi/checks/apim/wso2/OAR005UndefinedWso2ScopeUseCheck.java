package apiquality.sonar.openapi.checks.apim.wso2;

import com.google.common.collect.ImmutableSet;
import com.sonar.sslr.api.AstNode;
import com.sonar.sslr.api.AstNodeType;
import org.sonar.check.Rule;
import org.apiaddicts.apitools.dosonarapi.api.v2.OpenApi2Grammar;
import org.apiaddicts.apitools.dosonarapi.api.v3.OpenApi3Grammar;
import apiquality.sonar.openapi.checks.BaseCheck;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Rule(key = OAR005UndefinedWso2ScopeUseCheck.KEY)
public class OAR005UndefinedWso2ScopeUseCheck extends BaseCheck {

	public static final String KEY = "OAR005";
	private static final String MESSAGE = "OAR005.error";

	private Set<String> definedScopes;

	@Override
	protected void visitFile(JsonNode root) {
		definedScopes = getScopes(root);
	}

	private Set<String> getScopes(JsonNode root) {
		JsonNode scopes = root.get("x-wso2-security").get("apim").get("x-wso2-scopes");
		if (scopes.isMissing() || scopes.isNull()) return Collections.emptySet();
		return scopes
				.elements()
				.stream()
				.map(node -> node.get("name"))
				.filter(node -> !node.isMissing() && !node.isNull())
				.map(AstNode::getTokenValue)
				.collect(Collectors.toSet());
	}

	@Override
	public Set<AstNodeType> subscribedKinds() {
		return ImmutableSet.of(OpenApi2Grammar.OPERATION, OpenApi3Grammar.OPERATION);
	}

	@Override
	public void visitNode(JsonNode node) {
		visitV2Node(node);
	}

	private void visitV2Node(JsonNode node) {
		JsonNode scopeNode = node.get("x-scope");
		if (scopeNode.isMissing()) return;
		String scope = scopeNode.isNull() ? null : scopeNode.getTokenValue();
		if (isNull(scope) || !definedScopes.contains(scope)) {
			addIssue(KEY, translate(MESSAGE), scopeNode);
		}
	}

}
