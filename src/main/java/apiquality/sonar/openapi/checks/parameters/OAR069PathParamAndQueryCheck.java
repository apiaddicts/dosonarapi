package apiquality.sonar.openapi.checks.parameters;

import com.sonar.sslr.api.AstNodeType;
import org.apiaddicts.apitools.dosonarapi.api.v2.OpenApi2Grammar;
import org.apiaddicts.apitools.dosonarapi.api.v3.OpenApi3Grammar;
import org.sonar.check.Rule;
import apiquality.sonar.openapi.checks.BaseCheck;
import com.google.common.collect.ImmutableSet;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Rule(key = OAR069PathParamAndQueryCheck.KEY)
public class OAR069PathParamAndQueryCheck extends BaseCheck {

    public static final String KEY = "OAR069";
    private static final String MESSAGE = "OAR069.error";

    @Override
    public Set<AstNodeType> subscribedKinds() {
        return ImmutableSet.of(OpenApi2Grammar.OPERATION, OpenApi3Grammar.OPERATION);
    }

    @Override
    public void visitNode(JsonNode node) {
        if (node.getType() == OpenApi2Grammar.OPERATION || node.getType() == OpenApi3Grammar.OPERATION) {
            visitOperationNode(node);
        }
    }

    private void visitOperationNode(JsonNode node) {
        List<JsonNode> parameters = node.getDescendants(OpenApi2Grammar.PARAMETER, OpenApi3Grammar.PARAMETER)
            .stream()
            .map(JsonNode.class::cast)
            .collect(Collectors.toList());
    
        for (JsonNode parameterNode : parameters) {
            if (isPathOrQueryParameter(parameterNode)) {
                JsonNode responsesNode = node.get("responses");
                if (responsesNode == null) {
                    addIssue(KEY, translate(MESSAGE), parameterNode);
                } else {
                    JsonNode node400 = responsesNode.get("400");
                    if (node400.isMissing() || node400.isNull()) {
                        addIssue(KEY, translate(MESSAGE), parameterNode);
                    }
                }
            }
        }
    }

    private boolean isPathOrQueryParameter(JsonNode parameterNode) {
        JsonNode inNode = parameterNode.get("in");
        return inNode != null && ("query".equals(inNode.getTokenValue()) || "path".equals(inNode.getTokenValue()));
    }
}
