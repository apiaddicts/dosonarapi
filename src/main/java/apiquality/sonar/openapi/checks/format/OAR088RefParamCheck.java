package apiquality.sonar.openapi.checks.format;

import com.google.common.collect.ImmutableSet;
import com.sonar.sslr.api.AstNodeType;
import org.sonar.check.Rule;
import org.sonar.check.RuleProperty;
import org.apiaddicts.apitools.dosonarapi.api.v2.OpenApi2Grammar;
import org.apiaddicts.apitools.dosonarapi.api.v3.OpenApi3Grammar;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;
import apiquality.sonar.openapi.checks.BaseCheck;

import java.util.Set;

@Rule(key = OAR088RefParamCheck.KEY)
public class OAR088RefParamCheck extends BaseCheck {

    public static final String KEY = "OAR088";
    private static final String MESSAGE = "OAR088.error";
    private static final String DEFAULT_SUFFIX = "Param";

    @RuleProperty(
            key = "default-suffix",
            description = "Suffix for the $ref in parameters",
            defaultValue = DEFAULT_SUFFIX
    )
    private String paramSuffix = DEFAULT_SUFFIX;

    @Override
    public Set<AstNodeType> subscribedKinds() {
        return ImmutableSet.of(OpenApi2Grammar.PATHS, OpenApi3Grammar.PATHS);
    }

    @Override
    public void visitNode(JsonNode node) {
        if (OpenApi2Grammar.PATHS.equals(node.getType()) || OpenApi3Grammar.PATHS.equals(node.getType())) {
            visitPathsNode(node);
        }
    }

    private void visitPathsNode(JsonNode pathsNode) {
        for (JsonNode pathNode : pathsNode.propertyMap().values()) {
            for (JsonNode operationNode : pathNode.propertyMap().values()) {
                JsonNode parametersNode = operationNode.get("parameters");
                if (parametersNode != null && !parametersNode.isMissing()) {
                    for (JsonNode parameterNode : parametersNode.elements()) {
                        visitParameterNode(parameterNode);
                    }
                }
            }
        }
    }

    private void visitParameterNode(JsonNode parameterNode) {
        JsonNode refNode = parameterNode.get("$ref");
        if (refNode != null && !refNode.isMissing()) {
            String refValue = refNode.getTokenValue();
            if (!refValue.endsWith(paramSuffix)) {
                addIssue(KEY, translate(MESSAGE, paramSuffix), refNode.key());
            }
        }
    }
}