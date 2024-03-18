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

@Rule(key = OAR030StatusEndpointCheck.KEY)
public class OAR030StatusEndpointCheck extends BaseCheck {

    public static final String KEY = "OAR030";
    private static final String DEFAULT_STATUS_ENDPOINT = "/status";
    private static final String DEFAULT_METHOD = "get";

    @RuleProperty(
            key = "status-endpoint",
            description = "Status endpoint to validate.",
            defaultValue = DEFAULT_STATUS_ENDPOINT)
    public String statusEndpoint = DEFAULT_STATUS_ENDPOINT;

    @RuleProperty(
            key = "method",
            description = "Status endpoint method to validate.",
            defaultValue = DEFAULT_METHOD)
    public String method = DEFAULT_METHOD;

    private boolean endpointFound = false;

    @Override
    public Set<AstNodeType> subscribedKinds() {
        return ImmutableSet.of(OpenApi2Grammar.PATH, OpenApi3Grammar.PATH);
    }

    @Override
    protected void visitFile(JsonNode root) {
        endpointFound = false;
    }

    @Override
    protected void leaveFile(JsonNode node) {
        if (!endpointFound) {
            addIssue(KEY, translate("OAR030.error-path", statusEndpoint), node.get("paths").key());
        }
    }

    @Override
    public void visitNode(JsonNode node) {
        visitV2Node(node);
    }

    private void visitV2Node(JsonNode node) {
        if (statusEndpoint.equals(node.key().getTokenValue())) {
            endpointFound = true;
            if (node.value().get(method).isMissing()) {
                addIssue(KEY, translate("OAR030.error-verb", method), node.key());
            }
        }
    }
}
