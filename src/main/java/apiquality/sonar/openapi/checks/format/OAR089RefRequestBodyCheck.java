package apiquality.sonar.openapi.checks.format;

import com.google.common.collect.ImmutableSet;
import com.sonar.sslr.api.AstNodeType;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.apiaddicts.apitools.dosonarapi.api.v3.OpenApi3Grammar;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;
import apiquality.sonar.openapi.checks.BaseCheck;

import java.util.Set;

@Rule(key = OAR089RefRequestBodyCheck.KEY)
public class OAR089RefRequestBodyCheck extends BaseCheck {

    public static final String KEY = "OAR089";
    private static final String MESSAGE = "OAR089.error";
    private static final String DEFAULT_SUFFIX = "Body";

    @RuleProperty(
            key = "default-suffix",
            description = "Suffix for the $ref in requestBody",
            defaultValue = DEFAULT_SUFFIX
    )
    private String requestBodySuffix = DEFAULT_SUFFIX;

    @Override
    public Set<AstNodeType> subscribedKinds() {
        return ImmutableSet.of(OpenApi3Grammar.PATHS);
    }

    @Override
    public void visitNode(JsonNode node) {
        if (OpenApi3Grammar.PATHS.equals(node.getType())) {
            visitPathsNode(node);
        }
    }

    private void visitPathsNode(JsonNode pathsNode) {
        for (JsonNode pathNode : pathsNode.propertyMap().values()) {
            for (JsonNode operationNode : pathNode.propertyMap().values()) {
                JsonNode requestBodyNode = operationNode.get("requestBody");
                if (requestBodyNode != null && !requestBodyNode.isMissing()) {
                    visitRequestBodyNode(requestBodyNode);
                }
            }
        }
    }

    private void visitRequestBodyNode(JsonNode requestBodyNode) {
        JsonNode refNode = requestBodyNode.get("$ref");
        if (refNode != null && !refNode.isMissing()) {
            String refValue = refNode.getTokenValue();
            if (!refValue.endsWith(requestBodySuffix)) {
                addIssue(KEY, translate(MESSAGE, requestBodySuffix), refNode.key());
            }
        }
    }
}