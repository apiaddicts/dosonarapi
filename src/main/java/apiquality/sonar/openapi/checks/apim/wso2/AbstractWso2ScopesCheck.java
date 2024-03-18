package apiquality.sonar.openapi.checks.apim.wso2;

import com.google.common.collect.ImmutableSet;
import com.sonar.sslr.api.AstNodeType;
import org.apiaddicts.apitools.dosonarapi.api.v2.OpenApi2Grammar;
import org.apiaddicts.apitools.dosonarapi.api.v3.OpenApi3Grammar;
import apiquality.sonar.openapi.checks.BaseCheck;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

import java.util.List;
import java.util.Set;

public abstract class AbstractWso2ScopesCheck extends BaseCheck {

	@Override
	public Set<AstNodeType> subscribedKinds() {
		return ImmutableSet.of(OpenApi2Grammar.ROOT, OpenApi3Grammar.ROOT);
	}

	@Override
	public void visitNode(JsonNode node) {
		visitV2NV3Node(node);
	}

	private void visitV2NV3Node(JsonNode node) {
		JsonNode scopesNode = node.get("x-wso2-security").get("apim").get("x-wso2-scopes");
		visitScopesNode(scopesNode);
		if (scopesNode.isMissing() || scopesNode.isNull()) return;
		List<JsonNode> scopes = scopesNode.elements();
		visitScopes(scopes);
		scopes.forEach(this::visitScope);
	}

	protected void visitScopesNode(JsonNode scopesNode) {
		// Intentional blank
	}

	protected void visitScopes(List<JsonNode> scopes) {
		// Intentional blank
	}

	protected abstract void visitScope(JsonNode scope);
}
