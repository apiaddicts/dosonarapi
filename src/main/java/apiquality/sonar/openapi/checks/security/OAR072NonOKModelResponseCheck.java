package apiquality.sonar.openapi.checks.security;

import com.google.common.collect.ImmutableSet;
import com.sonar.sslr.api.AstNodeType;
import org.sonar.check.Rule;
import org.apiaddicts.apitools.dosonarapi.api.v2.OpenApi2Grammar;
import org.apiaddicts.apitools.dosonarapi.api.v3.OpenApi3Grammar;
import apiquality.sonar.openapi.checks.BaseCheck;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

import java.util.Set;

@Rule(key = OAR072NonOKModelResponseCheck.KEY)
public class OAR072NonOKModelResponseCheck extends BaseCheck {

    public static final String KEY = "OAR072";
    private static final String MESSAGE = "OAR072.error";

    private static final Set<String> ALLOWED_RESPONSE_CODES = ImmutableSet.of(
            "200", "201", "202", "203", "204", "205", "206", "207", "208", "226", "250"
    );

    @Override
    public Set<AstNodeType> subscribedKinds() {
        return ImmutableSet.of(OpenApi2Grammar.OPERATION, OpenApi3Grammar.OPERATION);
    }

    @Override
    public void visitNode(JsonNode node) {
        visitOperationNode(node);
    }

    private void visitOperationNode(JsonNode node) {
    JsonNode responsesNode = node.get("responses");
    if (responsesNode != null) {
        for (JsonNode responseNode : responsesNode.propertyMap().values()) {
            String responseCode = responseNode.key().getTokenValue();
            if (!ALLOWED_RESPONSE_CODES.contains(responseCode)) {
                if (node.is(OpenApi2Grammar.OPERATION)) {
                    checkSwaggerResponse(responseNode);
                } else if (node.is(OpenApi3Grammar.OPERATION)) {
                    checkOpenApiResponse(responseNode);
                }
            }
        }
    }
}

    private void checkSwaggerResponse(JsonNode jsonNode) {
        JsonNode schemaNode = jsonNode.get("schema");
        if (schemaNode != null) {
            JsonNode propertiesNode = schemaNode.get("properties");
            if (propertiesNode != null) {
                JsonNode stackTraceNode = propertiesNode.get("stackTrace");
                if (stackTraceNode.isMissing()) {
                    return;
                } else {
                    addIssue(KEY, translate(MESSAGE), stackTraceNode.key());
                }
            }
        }
    }

    private void checkOpenApiResponse(JsonNode jsonNode) {
        JsonNode contentNode = jsonNode.get("content");
        if (contentNode != null) {
            JsonNode mediaTypeNode = contentNode.get("application/json");
            if (mediaTypeNode != null) {
                JsonNode schemaNode = mediaTypeNode.get("schema");
                if (schemaNode != null) {
                    JsonNode propertiesNode = schemaNode.get("properties");
                    if (propertiesNode != null) {
                        JsonNode stackTraceNode = propertiesNode.get("stackTrace");
                        if (stackTraceNode.isMissing()) {
                            return;
                        } else {
                            addIssue(KEY, translate(MESSAGE), stackTraceNode.key());
                        }
                    }
                }
            }
        }
    }
}