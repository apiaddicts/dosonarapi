package apiquality.sonar.openapi.checks.security;

import com.google.common.collect.ImmutableSet;
import com.sonar.sslr.api.AstNodeType;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.apiaddicts.apitools.dosonarapi.api.v2.OpenApi2Grammar;
import org.apiaddicts.apitools.dosonarapi.api.v3.OpenApi3Grammar;
import apiquality.sonar.openapi.checks.BaseCheck;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Set;

@Rule(key = OAR054HostCheck.KEY)
public class OAR054HostCheck extends BaseCheck {

	public static final String KEY = "OAR054";
    
    private static final String HOST_REGEX = "^((\\*|[\\w\\d]+(-[\\w\\d]+)*)\\.)*(apiaddicts)(\\.org)$";

    @RuleProperty(
            key = "host-regex",
            description = "Host regex.",
            defaultValue = HOST_REGEX
    )
    private String hostRegex = HOST_REGEX;

	@Override
	public Set<AstNodeType> subscribedKinds() {
		return ImmutableSet.of(OpenApi2Grammar.ROOT, OpenApi3Grammar.SERVER);
	}

	@Override
	public void visitNode(JsonNode node) {
		if (node.getType() instanceof OpenApi2Grammar) {
			visitV2Node(node);
		} else {
			visitV3Node(node);
		}
	}

	private void visitV2Node(JsonNode node) {
        JsonNode hostNode = node.get("host");
		if (hostNode.isMissing() || hostNode.isNull()) {
			addIssue(KEY, translate("OAR054.host-mandatory"), node.key());
		} else {
            String host = hostNode.getTokenValue();
            validateHost(host, hostNode);
		}
	}

	private void visitV3Node(JsonNode node) {
        JsonNode urlNode = node.get("url");
        String server = urlNode.getTokenValue();
		try {
            String host = new URL(server).getHost();
            validateHost(host, urlNode);
		} catch (MalformedURLException e) {
            addIssue(KEY, translate("generic.malformed-url"), urlNode.value());
		}
    }
    
    private void validateHost(String host, JsonNode node) {
        if (!host.matches(hostRegex)){
            addIssue(KEY, translate("OAR054.host-format"), node.value());
        }
    }
}
