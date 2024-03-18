package apiquality.sonar.openapi.checks.operations;

import com.google.common.collect.ImmutableSet;
import com.sonar.sslr.api.AstNodeType;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.apiaddicts.apitools.dosonarapi.api.v2.OpenApi2Grammar;
import org.apiaddicts.apitools.dosonarapi.api.v3.OpenApi3Grammar;
import apiquality.sonar.openapi.checks.BaseCheck;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Rule(key = OAR008AllowedHttpVerbCheck.KEY)
public class OAR008AllowedHttpVerbCheck extends BaseCheck {

	public static final String KEY = "OAR008";
	private static final String MESSAGE = "OAR008.error";
	private static final String DEFAULT_ALLOWED_VERBS_VALUE = "get,post,put,delete,patch";

	@RuleProperty(
			key = "allowed-verbs",
			description = "List of allowed http verbs separated by coma.",
			defaultValue = DEFAULT_ALLOWED_VERBS_VALUE)
	public String allowedVerbsStr = DEFAULT_ALLOWED_VERBS_VALUE;

	private Set<String> allowedVerbs;

	@Override
	protected void visitFile(JsonNode root) {
		allowedVerbs = Stream.of(allowedVerbsStr.split(","))
				.map(String::trim)
				.map(String::toLowerCase)
				.collect(Collectors.toSet());
	}

	@Override
	public Set<AstNodeType> subscribedKinds() {
		return ImmutableSet.of(OpenApi2Grammar.OPERATION, OpenApi3Grammar.OPERATION);
	}

	@Override
	public void visitNode(JsonNode node) {
		visitOperationNode(node);
	}

	private void visitOperationNode(JsonNode node) {
		String httpVerb = node.key().getTokenValue();
		if (!"$ref".equals(httpVerb) && !allowedVerbs.contains(httpVerb)) {
			addIssue(KEY, translate(MESSAGE, httpVerb), node.key());
		}
	}
}
