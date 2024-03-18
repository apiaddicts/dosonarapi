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

@Rule(key = OAR093ResponseOnlyRefCheck.KEY)
public class OAR093ResponseOnlyRefCheck extends BaseCheck {

    public static final String KEY = "OAR093";
    private static final String MESSAGE = "OAR093.error";

    @Override
    public Set<AstNodeType> subscribedKinds() {
        return ImmutableSet.of(OpenApi2Grammar.PATHS, OpenApi3Grammar.PATHS);
    }

    @Override
    public void visitNode(JsonNode node) {
        if (OpenApi2Grammar.PATHS.equals(node.getType()) || OpenApi3Grammar.PATHS.equals(node.getType())) {
            visitResponseNode(node);
        }
    }

    private void visitResponseNode(JsonNode pathsNode) {
        for (JsonNode pathNode : pathsNode.propertyMap().values()) { 
            for (JsonNode operationNode : pathNode.propertyMap().values()) { 
                JsonNode responses = operationNode.get("responses");
                if (responses != null) {
                    for (JsonNode responseNode : responses.propertyMap().values()) { 
                        if (!responseNode.propertyMap().keySet().equals(Collections.singleton("$ref"))) {
                            addIssue(KEY, translate(MESSAGE), responses.key());
                        }
                    }
                }
            }
        }
    }
}