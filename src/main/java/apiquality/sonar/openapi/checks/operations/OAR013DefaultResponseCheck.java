package apiquality.sonar.openapi.checks.operations;

import com.google.common.collect.ImmutableSet;
import com.sonar.sslr.api.AstNodeType;
import org.sonar.check.Rule;
import org.apiaddicts.apitools.dosonarapi.api.v2.OpenApi2Grammar;
import org.apiaddicts.apitools.dosonarapi.api.v3.OpenApi3Grammar;
import apiquality.sonar.openapi.checks.BaseCheck;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

import java.util.Set;

@Rule(key = OAR013DefaultResponseCheck.KEY)
public class OAR013DefaultResponseCheck extends BaseCheck {

	public static final String KEY = "OAR013";
	private static final String MESSAGE = "OAR013.error";

	@Override
	public Set<AstNodeType> subscribedKinds() {
		return ImmutableSet.of(OpenApi2Grammar.RESPONSES, OpenApi3Grammar.RESPONSES);
	}

	@Override
	public void visitNode(JsonNode node) {
		visitV2Node(node);
	}

	private void visitV2Node(JsonNode node) {
		if (node.get("default").isMissing()) {
			addIssue(KEY, translate(MESSAGE), node.key());
		}
	}
}
