package apiquality.sonar.openapi.checks.operations;

import com.google.common.collect.ImmutableSet;
import com.sonar.sslr.api.AstNodeType;
import org.sonar.check.Rule;
import org.apiaddicts.apitools.dosonarapi.api.v2.OpenApi2Grammar;
import org.apiaddicts.apitools.dosonarapi.api.v3.OpenApi3Grammar;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;
import apiquality.sonar.openapi.checks.BaseCheck;

import java.util.Collections;
import java.util.Set;

@Rule(key = OAR091ParamOnlyRefCheck.KEY)
public class OAR091ParamOnlyRefCheck extends BaseCheck {

    public static final String KEY = "OAR091";
    private static final String MESSAGE = "OAR091.error";

    @Override
    public Set<AstNodeType> subscribedKinds() {
        return ImmutableSet.of(OpenApi2Grammar.PATHS, OpenApi3Grammar.PATHS);
    }

    @Override
    public void visitNode(JsonNode node) {
        if (OpenApi2Grammar.PATHS.equals(node.getType()) || OpenApi3Grammar.PATHS.equals(node.getType())) {
            visitParametersNode(node);
        }
    }

    private void visitParametersNode(JsonNode pathsNode) {
        for (JsonNode pathNode : pathsNode.propertyMap().values()) { 
            for (JsonNode operationNode : pathNode.propertyMap().values()) { 
                JsonNode parameters = operationNode.get("parameters");
                if (parameters != null) {
                    for (JsonNode paramNode : parameters.elements()) { 
                        if (!paramNode.propertyMap().keySet().equals(Collections.singleton("$ref"))) {
                            addIssue(KEY, translate(MESSAGE), parameters.key());
                        }
                    }
                }
            }
        }
    }
}






