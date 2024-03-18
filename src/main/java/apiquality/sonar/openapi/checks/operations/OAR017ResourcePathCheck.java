package apiquality.sonar.openapi.checks.operations;

import com.google.common.collect.ImmutableSet;
import com.sonar.sslr.api.AstNodeType;
import org.sonar.check.Rule;
import org.apiaddicts.apitools.dosonarapi.api.v2.OpenApi2Grammar;
import org.apiaddicts.apitools.dosonarapi.api.v3.OpenApi3Grammar;
import apiquality.sonar.openapi.checks.BaseCheck;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

import java.util.Set;
import java.util.stream.Stream;

@Rule(key = OAR017ResourcePathCheck.KEY)
public class OAR017ResourcePathCheck extends BaseCheck {

	public static final String KEY = "OAR017";
	private static final String MESSAGE = "OAR017.error";

	@Override
	public Set<AstNodeType> subscribedKinds() {
		return ImmutableSet.of(OpenApi2Grammar.PATH, OpenApi3Grammar.PATH);
	}

	@Override
	public void visitNode(JsonNode node) {
		visitV2Node(node);
	}

	private void visitV2Node(JsonNode node) {
		String path = node.key().getTokenValue();
		if (!isCorrect(path)) addIssue(KEY, translate(MESSAGE), node.key());
	}

	private boolean isCorrect(String path) {
		String[] parts = Stream.of(path.split("/")).filter(p -> !p.trim().isEmpty()).toArray(String[]::new);
		if (parts.length == 0) return true;

		boolean previousWasVariable = false;
		boolean twoOrMoreVariablesInARow = false;

		for (int i = 0; i < parts.length; i++) {
		boolean currentIsVariable = isVariable(parts[i]);

			if (previousWasVariable && currentIsVariable) {
				twoOrMoreVariablesInARow = true;
				break;
			}

			previousWasVariable = currentIsVariable;
		}

		return !twoOrMoreVariablesInARow;
	}

	private boolean isVariable(String part) {
		return '{' == part.charAt(0) && '}' == part.charAt(part.length() - 1);
	}
}