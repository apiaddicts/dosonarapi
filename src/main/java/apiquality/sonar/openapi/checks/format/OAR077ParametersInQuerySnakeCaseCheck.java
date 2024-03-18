package apiquality.sonar.openapi.checks.format;

import com.google.common.collect.ImmutableSet;
import com.sonar.sslr.api.AstNodeType;
import org.sonar.check.Rule;
import org.apiaddicts.apitools.dosonarapi.api.v2.OpenApi2Grammar;
import org.apiaddicts.apitools.dosonarapi.api.v3.OpenApi3Grammar;
import org.apiaddicts.apitools.dosonarapi.sslr.yaml.grammar.JsonNode;
import java.util.Set;


@Rule(key = OAR077ParametersInQuerySnakeCaseCheck.KEY)
public class OAR077ParametersInQuerySnakeCaseCheck extends AbstractNamingConventionCheck {

    public static final String KEY = "OAR077";
    private static final String MESSAGE = "OAR077.error";
    private static final String NAMING_CONVENTION = SNAKE_CASE;

    public OAR077ParametersInQuerySnakeCaseCheck() {
        super(KEY, MESSAGE, NAMING_CONVENTION);
    }

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
                if (!parametersNode.isMissing()) {
                    for (JsonNode parameterNode : parametersNode.elements()) {
                        if ("query".equals(parameterNode.get("in").getTokenValue())) {
                            visitParameterNode(parameterNode);
                        }
                    }
                }
            }
        }
    }

    private void visitParameterNode(JsonNode parameterNode) {
        JsonNode nameNode = parameterNode.get("name");
        if (!nameNode.isMissing()) {
            String name = nameNode.getTokenValue();
            validateNamingConvention(name, nameNode);
        }
    }
}